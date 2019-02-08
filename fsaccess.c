#include<stdio.h>
#include<sys/types.h>
#include<unistd.h>
#include<errno.h>
#include<fcntl.h>
#include<string.h>
#include<stdlib.h>
#include<math.h>
#include <libgen.h>


typedef struct{
int blk_number;	
}logical_data_blk;

typedef struct {//super block utilization-1023 bytes
unsigned int isize;
unsigned int fsize;
unsigned int nfree;
unsigned int free[150];
unsigned int ninode;
unsigned int inode[100];
char flock;
char ilock;
char fmod;
unsigned short time[2];
}superblock;

typedef struct {//inode size-64bytes
unsigned short flags;
unsigned short nlinks;
unsigned short uid;
unsigned short gid;
unsigned short size0; // -2 bytes of size
unsigned short size1;//-2 bytes of size-toatal 4 bytes,makes max 4gb file possible
unsigned int addr[12];  //one level of triple indirect block can support 4gb.
unsigned short actime;
unsigned short modtime[2];
}inode;

typedef struct {
unsigned short inode_number;//inode which is allocated to the directory 
char name[14];//name corresponding to the directory
}directory;

//free array structure for chaining the blocks
typedef struct {
unsigned int nfree;
unsigned int free[150];
}freeArray;


int initfs(int fd,unsigned short num_blck,unsigned short num_of_inodes ){
superblock super;

//setting fsize;
super.fsize=num_blck;
int number_of_blocks=num_blck;
int inodesize=64;
int inode_mem=num_of_inodes*inodesize;
int blocksize=1024;
int blocks_into_freearray;
int inodes_into_inodearray;
//calculating iszie
if(inode_mem%blocksize==0){
 super.isize=inode_mem/blocksize;
}
else{
super.isize=inode_mem/blocksize+1;
}
/*isize number of blocks to inodes,1st two blocks are bootloader and superblock 
and the 1st data block is allocated to the contents of root dierctory*/

int filled_blocks=3+super.isize;
int remaining_blocks=num_blck-filled_blocks;

/*setting free array and nfree*/

super.nfree=150;//setting nfree as 150
//free blocks are less than 150
if(remaining_blocks<150){
//setting nfree
blocks_into_freearray=remaining_blocks;
remaining_blocks=0;
}
//free blocks are more than 150
else if(remaining_blocks>150){
blocks_into_freearray=150;
remaining_blocks=remaining_blocks-150;
}
for(int i=0;i<blocks_into_freearray;i++){
super.free[i]=filled_blocks+i;
}

/*setting nionode and inode array*/

super.ninode=100;//number of free inodes in the list of inodes
int first_free_inode_number=2;//inode 1 will be allocated during the initialization of the root directory
if(num_of_inodes<=100){
inodes_into_inodearray=num_of_inodes-1;
}
else if(num_of_inodes>100){
inodes_into_inodearray=100;
}
super.ninode=inodes_into_inodearray;
for(int j=0;j<inodes_into_inodearray;j++){
super.inode[j]=first_free_inode_number+j;
}
super.fmod = 'N';
super.flock = 'Y';
super.ilock='N';
lseek(fd,1024,SEEK_SET);//moving to the superblock in the file
write(fd, &super, 1024);//write the superblock in the next 1024 bytes of the file which will be the superblock
superblock s2;
lseek(fd,1024,SEEK_SET);
read(fd,&s2,1024);
printf("s2 components: isize = %d, fsize = %d,nfree= %d,ninode= %d\n",s2.isize, s2.fsize,s2.nfree,s2.ninode);//printing the initialized components
printf("printing superblock free array\n");
for(int i=0;i<150;i++){
printf("%d",s2.free[i]);//printing free array
printf(", ");
}
printf("\n");

/*initialize inode 1 with root directory information*/
initialize_rootdir(fd,filled_blocks);

/*link the free array for chaining free data blocks*/
chainfreearray(fd,remaining_blocks,filled_blocks);

/*testing chaining of free blocks*/
print_next_set_of_chainedblocks(fd,filled_blocks,remaining_blocks);//to test if chaining is correct

return 1;
}

void print_next_set_of_chainedblocks(int fd, int filled_blocks,int remaining_blocks){
int to_add_to_free_array;
while(remaining_blocks>0){
if(remaining_blocks<150){
	to_add_to_free_array=remaining_blocks;
	remaining_blocks=0;
}
else{
	to_add_to_free_array=150;
	remaining_blocks=remaining_blocks-150;
}
lseek(fd,filled_blocks*1024,SEEK_SET);//move to first data block which conatins the list of next set of free datablocks
freeArray f1;
read(fd,&f1,604);
printf("chained free array\n");
printf("nfree value: %d\n",f1.nfree);
for(int i=0;i<to_add_to_free_array;i++){
	printf("%d",f1.free[i]);
	printf(", ");
}
printf("\n");
filled_blocks=filled_blocks+150;
}
} 

//link the free array for chaining free data blocks
void chainfreearray(int fd,int remaining_blocks,int filled_blocks)
{
freeArray freearr;
int to_add_data_blocks;// number of datablocks to be added to freelist
while(remaining_blocks>0){//whenever data blocks remain
lseek(fd,(filled_blocks*1024),SEEK_SET);//move pointer to the first available data block
filled_blocks+=150;//increment the freeblocks by the freearray size
if(remaining_blocks<150){
to_add_data_blocks=remaining_blocks;
freearr.nfree = remaining_blocks;
remaining_blocks = 0;
}
else{
to_add_data_blocks=150;
freearr.nfree =150;
remaining_blocks =remaining_blocks-150;
}
for(int k=0;k<to_add_data_blocks;k++){
freearr.free[k]=filled_blocks+k;
}

write(fd, &freearr,604);

}
}

void initialize_rootdir(int fd,int filled_blocks){
inode inodestr;//instance of inode struct
//setting inode flags
inodestr.flags=140777;//1-inode is allocated,4-directory,0-userid and groupid on execution not provided,each 7-r,w,execute to ownwner,group,others
inodestr.nlinks = '2';
inodestr.uid = '0';
inodestr.gid = '0';
inodestr.size0 = '0';
inodestr.size1 = 16*2;
inodestr.addr[0] = filled_blocks-1;//alloting first data block to inode 1's addr[] array.
printf("addrdatablk:%d\n",inodestr.addr[0]);
inodestr.actime = 0;
inodestr.modtime[0] = 0;
inodestr.modtime[1] = 0;
lseek(fd, 2*1024,SEEK_SET);//going to the first inode block position
write(fd, &inodestr,64);//writing the root directory information into the inode1 which is the first 64 bytes
//init root directory's data block
directory dir;
dir.inode_number = 1;//setting inode number for the root to be 1
strncpy(dir.name, ".", 14);//cpying '.' in the name field of the directory
int logical_datablockstomove=filled_blocks-1;//first datablock which corresponds to the root
int totalbytes=logical_datablockstomove*1024;
lseek(fd,totalbytes,SEEK_SET);//moving to the first datablock which corresponds to root directory
write(fd, &dir, 16);//writing the directory structure there
directory dir2;
lseek(fd,totalbytes,SEEK_SET);
read(fd,&dir2,16);
printf("priniting root directory data\n");//verifying root directory information by read from file
printf("name: %s\n",dir2.name);
printf("inode_number: %d\n",dir2.inode_number);
strncpy(dir.name, "..", 14);//setting parent directory name
int to_seek=totalbytes+16;
lseek(fd,to_seek,SEEK_SET);
write(fd, &dir, 16);//writing the directory again
directory dir3;
lseek(fd,to_seek,SEEK_SET);
read(fd,&dir3,16);//verifying root info by reading from file
printf("priniting parent of root directory data\n");
printf("name: %s\n",dir3.name);
printf("inode_number: %d\n",dir3.inode_number);


}

unsigned short allocate_inode(int fd){
	
	unsigned short inode_number;
	lseek(fd,1024,SEEK_SET);
	superblock super;
	read(fd,&super,1024);
	if(super.ninode>0){
	super.ninode--;
	inode_number=super.inode[super.ninode];
	lseek(fd,2660,SEEK_SET);
	write(fd,&super.ninode,2);
	}else{
		printf("no inode free");
		exit(1);
	}
	return inode_number;
	
}

int allocate_data_block(int fd){
int blocknum;
	lseek(fd, 1024, SEEK_SET);
	//printf("lseekval inside func:%d\n",x);
	superblock super;
	read(fd, &super, 1024);
	if(super.nfree != 1){
		//printf("inside if\n");
		super.nfree--;
		//printf("nfree:%d\n",super.nfree);
		blocknum = super.free[super.nfree];	
		//printf("blocnum:%d\n",blocknum);
		lseek(fd, 1024, SEEK_SET);
		write(fd, &super,1024);
	}
	else{
		//printf("inside else\n");
		super.nfree--;
		blocknum = super.free[super.nfree];	
		lseek(fd, blocknum *1024, 0);
		freeArray arr;
		read(fd, &arr, 604);
		lseek(fd, 1032, 0);
		write(fd, &arr, 604);
	}
		
	return blocknum;
	
}

void mkdir(int fd,char* dirname){//file system name and directory name
	//allocate inode to the dir
	directory dir;
	dir.inode_number=allocate_inode(fd);
	strncpy(dir.name, ".", 14);
	unsigned short inode_num=dir.inode_number;
	//write to new inode
	inode inodestr;
	inodestr.flags=140777;//1-inode is allocated,4-directory,0-userid and groupid on execution not provided,each 7-r,w,execute to ownwner,group,others
    inodestr.nlinks = '2';
    inodestr.uid = '0';
    inodestr.gid = '0';
    inodestr.size0 = '0';
    inodestr.size1 = 0;
    inodestr.addr[0] = allocate_data_block(fd);
    inodestr.actime = 0;
    inodestr.modtime[0] = 0;
    inodestr.modtime[1] = 0;
	lseek(fd,(2*1024+(inode_num-1)*64),SEEK_SET);
	write(fd,&inodestr,64);
	//write the directory name and parent directory name in the allocated data block
	lseek(fd,3+(inodestr.addr[0]*1024),SEEK_SET);
	write(fd,&dir,14);
	directory dir_par;
	dir_par.inode_number=1;
	strncpy(dir_par.name, ".", 14);
    lseek(fd,3+(inodestr.addr[0]*1024)+16,SEEK_SET);
	write(fd,&dir_par,16);
	//parse directory name
	char *ptr;
    int ch='/';
	ptr = strrchr(dirname,ch);
	ptr++;//final directory name
    //write directory name to parent directory which is the root directory
	superblock sup_dum;
	lseek(fd,1024,SEEK_SET);
	read(fd,&sup_dum,1024);
	int inode_size=sup_dum.isize;
    lseek(fd,(2+inode_size)*1024,SEEK_SET);
    directory dir_root;
	int entry_count=0;
    read(fd,&dir_root,16);
    while(dir_root.inode_number!=0){
    entry_count++;
	read(fd,&dir_root,16);
	}
	int lseek_val=(2+inode_size)*1024 + entry_count*16;
	lseek(fd,lseek_val,SEEK_SET);
	directory dir_to_write;
	strcpy(dir_to_write.name,ptr);
	dir_to_write.inode_number=inode_num;
	write(fd,&dir_to_write,16);
    //verifying the new_dir_creation
	lseek(fd,lseek_val,SEEK_SET);
	directory dirread;
	read(fd,&dirread,16);
	superblock s1;
    lseek(fd,1024,SEEK_SET);
    read(fd,&s1,1024);
    s1.ninode--;
    lseek(fd,1024,SEEK_SET);
    write(fd,&s1,1024);
	printf("name: %s\n",dirread.name);
    printf("inode_number: %d\n",dirread.inode_number);
	printf("new directory created\n");
	printf("ninode value: %d\n",s1.ninode);
	
	}
	
	int find_file_count(int fd,inode inode_dum){
	int file_count = 0;
	lseek(fd, (inode_dum.addr[0]*1024), 0);
	directory dir;
	read(fd, &dir, 16);
	while(dir.inode_number != 0){
		file_count++;
		read(fd, &dir, 16);
	}
	
	return file_count;
	}
	
	
void cpin(int fd,const char* from,const char* to){
	int data_block_level1[256];
	int data_block_level2[256];
	int data_block_level3[256];
	int data_blocks_allocated=0;
	char buf[1024];
    printf("from file: %s\n",from);
	printf("to file: %s\n",to);
    int from_fd=open(from, O_RDONLY);
	unsigned int ext_filesize=lseek(from_fd,0,SEEK_END);
	printf("filesize:%d\n",ext_filesize);
	lseek(from_fd,0,SEEK_SET);
	int b=ext_filesize;
	unsigned short inode_num=allocate_inode(fd);
	printf("allocated inode_number:%d\n",inode_num);
	inode inodestruct;
	if(ext_filesize<=12288){
	printf("copying small file\n");
	inodestruct.flags=0100777;
    inodestruct.nlinks = '2';
    inodestruct.uid = '0';
    inodestruct.gid = '0';
    inodestruct.size0 = '0';
    inodestruct.size1 = ext_filesize;
    inodestruct.actime = 0;
    inodestruct.modtime[0] = 0;
    inodestruct.modtime[1] = 0;
	int no_of_blocks_extf=ext_filesize/1024;
	int remaining=ext_filesize%1024;
	if(remaining>0){
	 for(int i=0;i<(no_of_blocks_extf+1);i++){
		 inodestruct.addr[i]=allocate_data_block(fd);
		 data_blocks_allocated++;
	     printf("Blocks allocated:%d\n",inodestruct.addr[i]);
		 //read from external file
		 lseek(from_fd,(i*1024),SEEK_SET);
		 read(from_fd,&buf,1024);
		 //write that to the data block location in v6 system
		 lseek(fd,inodestruct.addr[i]*1024,SEEK_SET);
		 write(fd,&buf,1024);
		 }
	}
	else{
		for(int i=0;i<no_of_blocks_extf;i++){
		inodestruct.addr[i]=allocate_data_block(fd);
		data_blocks_allocated++;
		//read from external file
		lseek(from_fd,(i*1024),SEEK_SET);
		read(from_fd,&buf,1024);
		//write that to the data block location in v6 system
		lseek(fd,inodestruct.addr[i]*1024,SEEK_SET);
		write(fd,&buf,1024);	
			}
		
	}
	printf("total data blocks in small v6 file:%d\n",data_blocks_allocated);
    printf("small file copied\n");
}
else{
	printf("copying large file\n");
	inodestruct.flags=110777;
    inodestruct.nlinks = '2';
    inodestruct.uid = '0';
    inodestruct.gid = '0';
    inodestruct.size0 = '0';
    inodestruct.size1 = ext_filesize;
    inodestruct.actime = 0;
    inodestruct.modtime[0] = 0;
    inodestruct.modtime[1] = 0;
	for(int i=0;i<11;i++){
	inodestruct.addr[i]=allocate_data_block(fd);
	data_blocks_allocated++;
	printf("datablocks_for_directblock_actualdatablocks:%d\n",inodestruct.addr[i]);
	//read from external file
	lseek(from_fd,(i*1024),SEEK_SET);
	read(from_fd,&buf,1024);
	//write that to the data block location in v6 system
	lseek(fd,inodestruct.addr[i]*1024,SEEK_SET);
	write(fd,&buf,1024);
	}
	unsigned int ReadBytes = 11 * 1024;
	logical_data_blk blk;
	inodestruct.addr[11]=allocate_data_block(fd);//4000 contains first set of data_blocks//indirect block
	printf("datablock allocated to addr[11] in large file%d\n",inodestruct.addr[11]);
    for(int j=0;j<256;j++){
	    data_block_level1[j]=allocate_data_block(fd);//need to fill this in #4000
		blk.blk_number=data_block_level1[j];
		lseek(fd,(inodestruct.addr[11]*1024+j*4),SEEK_SET);
		write(fd,&blk,4);
		  for(int k=0;k<256;k++){
	         printf("datablocks_level1_indirection:%d\n",data_block_level1[j]);
             data_block_level2[k]=allocate_data_block(fd);//first entry of #4000 points to 5000;
			 blk.blk_number=data_block_level2[k];
			 lseek(fd,((data_block_level1[j]*1024)+(k*4)),SEEK_SET);
			 write(fd,&blk,4);
			  for(int l=0;l<256;l++){
	             printf("datablocks_level2_indirection:%d\n",data_block_level2[k]);
			     if(ReadBytes<ext_filesize){
			     data_block_level3[l]=allocate_data_block(fd);//first entry of #5000 points to actual data block
				 lseek(fd,(data_block_level2[k]*1024+l*4),SEEK_SET);
				 blk.blk_number=data_block_level3[l];
				 write(fd,&blk,sizeof(logical_data_blk));
				 //printf("Just wrote blk_number = %d at address %d\n", blk.blk_number, (data_block_level2[k]*1024+l*4));
				 data_blocks_allocated++;
				 }
				 else{
				 printf("total data blocks in large v6 file:%d\n",data_blocks_allocated);
                 printf("large file read and copied\n");
				 goto writeinode;	 
				 }
			     printf("datablocks_level3_actualdatablocks:%d\n",data_block_level3[l]);
                 lseek(from_fd,(11+(data_blocks_allocated-1))*1024,SEEK_SET);
                 read(from_fd,&buf,1024);
			     ReadBytes+=1024;
			     lseek(fd,data_block_level3[l]*1024,SEEK_SET);
				 write(fd,&buf,1024);
				}
			
		}
	
	}

}
//need to write inode
writeinode:
lseek(fd,(2*1024+(inode_num-1)*64),SEEK_SET);	
write(fd,&inodestruct,64);
//need to write the file name in the parent directory which will be the root directory
char *ptr;
int ch='/';
ptr = strrchr(to,ch);
ptr++;
lseek(fd,2*1024,SEEK_SET);
inode dum_root;
read(fd,&dum_root,64);
lseek(fd,dum_root.addr[0]*1024,SEEK_SET);
//printf("lseekval cpin:%d\n",y);
directory dir;
int readbytes;
readbytes=read(fd,&dir,16);
int filecount=0;
while(dir.inode_number!=0){
filecount++;
readbytes+=read(fd,&dir,16);
}
lseek(fd,dum_root.addr[0]*1024+(readbytes-16),SEEK_SET);
dir.inode_number=inode_num;
strcpy(dir.name,ptr);
write(fd,&dir,16);
lseek(fd,dum_root.addr[0]*1024,SEEK_SET);
directory reader;
read(fd,&reader,16);
while(reader.inode_number!=0){
read(fd,&reader,16);
}
}

void cpout(int fd,char* from,char* to){
char buf[1024];
int fileexisits=0;
char *ptr;
int ch='/';
ptr = strrchr(from,ch);
ptr++;
int inodenumber;
inode root_inode;
lseek(fd,2*1024,SEEK_SET);
read(fd,&root_inode,64);
lseek(fd,root_inode.addr[0]*1024,SEEK_SET);
directory dir_root;
int read_bytes;
read_bytes=read(fd,&dir_root,16);
while(dir_root.inode_number!=0){
read_bytes+=read(fd,&dir_root,16);
if(strcmp(dir_root.name,ptr)==0){
inodenumber=dir_root.inode_number;
fileexisits=1;
}
}
if(fileexisits==0){
 printf("internal v6file does not exist");
 exit(1);
}
int tofd=open(to, O_RDWR | O_CREAT, 0777);
lseek(fd,2*1024+(inodenumber-1)*64,SEEK_SET);
inode v6fileinode;
read(fd,&v6fileinode,64);
printf("filesize:%d\n",v6fileinode.size1);
if(v6fileinode.size1<12288){
printf("copying small file\n");
//small file
if(v6fileinode.size1%1024==0){
int count=v6fileinode.size1/1024;
for(int i=0;i<count;i++){
//read from internal file
lseek(fd,v6fileinode.addr[i]*1024,SEEK_SET);
read(fd,&buf,1024);
//write to external file
lseek(tofd,i*1024,SEEK_SET);
write(tofd,&buf,1024);
}
}
else{
int count=v6fileinode.size1/1024 + 1;
for(int i=0;i<count;i++){
//read from internal file
lseek(fd,v6fileinode.addr[i]*1024,SEEK_SET);
read(fd,&buf,1024);
//write to external file
lseek(tofd,i*1024,SEEK_SET);
write(tofd,&buf,1024);
}
}
printf("copying small file completed\n");
}
else{
printf("copying large file\n");
//large file
for(int i=0;i<11;i++){
//read from internal file
lseek(fd,v6fileinode.addr[i]*1024,SEEK_SET);
read(fd,&buf,1024);
//write to external file
lseek(tofd,i*1024,SEEK_SET);
write(tofd,&buf,1024);
}
int indirect_adress=v6fileinode.addr[11];
//printf("Indirect addr = %d\n", indirect_adress);
unsigned int ReadBytes = 11 * 1024;
int data_blocks_read=0;
int logical_block_number;
logical_data_blk tempblk;
//printf("SIZEOF = %d\n", sizeof(logical_data_blk));
	for(int j=0;j<256;j++){
		printf("Lseek to %d\n", ((indirect_adress*1024)+(j*4)));
	    lseek(fd,((indirect_adress*1024)+(j*4)),SEEK_SET);
		read(fd,&tempblk,sizeof(logical_data_blk));
		int level1_addr = tempblk.blk_number;
        for(int k=0;k<256;k++){
			//printf("1st level k blk_number = %d\n", tempblk.blk_number);
            lseek(fd,((level1_addr*1024)+(k*4)),SEEK_SET);
			read(fd,&tempblk,sizeof(logical_data_blk));
			int level2_addr = tempblk.blk_number;
			//printf("NOW 2st blk_number = %d\n", tempblk.blk_number);
              for(int l=0;l<256;l++){
                 lseek(fd,((level2_addr*1024)+(l*4)),SEEK_SET);
				 int addrr = ((level2_addr*1024)+(l*4));
	             read(fd,&tempblk,sizeof(logical_data_blk));
				 //printf("3. 3rd blk_number = %d at addr %d\n", tempblk.blk_number, addrr);
				 printf("Reading block_number %d by lseeking to %d\n", tempblk.blk_number, ((tempblk.blk_number*1024)+(l*4)));
				 lseek(fd,(tempblk.blk_number*1024),SEEK_SET);
				 int read_size = read(fd,&buf,1024);
				 //printf("JUST read %d bytes\n", read_size);
				 ReadBytes+=read_size;
				 data_blocks_read++;
				 //printf("Read %d bytes at position %d\n", ReadBytes, (tempblk.blk_number*1024));
				 lseek(tofd,(11+(data_blocks_read-1))*1024,SEEK_SET); 
				 write(tofd,&buf,read_size);
				 if(ReadBytes>=v6fileinode.size1){
				 //printf("FINISHED\n");
				 goto complete; 
				 }
                }
			
		}
		
	}

complete:
printf("copying large file completed\n");

}
}

inode read_inode(int fd,int inode_num){
	
	inode inode_struct;
	lseek(fd,(2*1024)+(inode_num-1)*64,SEEK_SET);
	read(fd,&inode_struct,64);
	return inode_struct;
}

int write_inode(int fd,inode inode_struct,int inode_num){
	
	lseek(fd,(2*1024)+(inode_num-1)*64,SEEK_SET);
	write(fd,&inode_struct,64);
	return 0;
	
}

int get_inode_by_file_name(int fd,char* filename){
	
	int inode_num;
	inode dir_inode;
	directory dir_node;
	dir_inode=read_inode(fd,1);
	lseek(fd,dir_inode.addr[0]*1024,SEEK_SET);
	int no_of_entries=1024/16;
	for(int i=0;i<no_of_entries;i++){
		read(fd,&dir_node,16);
		if(strcmp(filename,dir_node.name)==0)
			return dir_node.inode_number;
	}
	return -1;
}



int rm(int fd,char filename[]){
	short inode_no=1;
	inode i_node,i_node1;;
	superblock s1;
	int file_inode;
	lseek(fd,1024,SEEK_SET);
    read(fd,&s1,1024);
	directory dire[64];
	int bytes_moved=2048+((inode_no-1)*64);
	lseek(fd,bytes_moved,SEEK_SET);
	read(fd,&i_node,64);
	int r=0;
	freeArray freear;
	for(int i=0;i<11;i++){
      int r=i_node.addr[i];
      lseek(fd,r*1024,SEEK_SET);
	  read(fd,dire,1024);
	  for(int j=0;j<256;j++){
		  if(strcmp(dire[j].name, filename)==0){
			  file_inode=dire[j].inode_number;
			 int bytes_moved=2048+((dire[j].inode_number-1)*64);
	         lseek(fd,bytes_moved,SEEK_SET);
			 read(fd,&i_node1,64);
			 while(r<11){
				 if((s1.nfree!=150)&&(i_node1.addr[j]!=0)){
				 s1.free[s1.nfree]=i_node1.addr[j];
				 s1.nfree++;
				 r++;
				 }
				 else if((s1.nfree==150)&&(i_node1.addr[j]!=0)){
					  freear.nfree=s1.nfree;
					  for(int loop = 0; loop < 150; loop++) {
                       freear.free[loop] = s1.free[loop];
                       }
			          lseek(fd,s1.free[0]*1024,SEEK_SET);
					  write(fd,&freear,604);
					  s1.nfree=1;
					  s1.free[0]=i_node.addr[j];
					   r++;   					
				 }
				 else if(i_node1.addr[j]==0){
					 goto label;
				 }
			 }
			 if(i_node1.addr[11]!=0){
				 int arr1[256];
				 lseek(fd,i_node1.addr[12]*1024,SEEK_SET);
			     read(fd,arr1,1024);
				 for(int n=0;n<256;n++){
					 int arr2[256];
					 lseek(fd,arr1[n]*1024,SEEK_SET);
			         read(fd,arr2,1024);
					 for(int m=0;m<256;n++){
					 int arr3[256];
					 lseek(fd,arr2[m]*1024,SEEK_SET);
			         read(fd,arr3,1024);
					 for(int l=0;l<256;l++){
					 int arr4[256];
					 lseek(fd,arr3[l]*1024,SEEK_SET);
			         read(fd,arr4,1024);
					 if((s1.nfree!=150)&&(arr4[l]!=0)){
				 s1.free[s1.nfree]=i_node1.addr[l];
				 s1.nfree++;
				 l++;
				 }
				 else if((s1.nfree==150)&&(arr4[l]!=0)){
					  freear.nfree=s1.nfree;
					  for(int loop = 0; loop < 150; loop++) {
                       freear.free[loop] = s1.free[loop];
                       }
			          lseek(fd,s1.free[0]*1024,SEEK_SET);
					  write(fd,&freear,604);
					  s1.nfree=1;
					  s1.free[0]=i_node.addr[l];
					   l++;   					
				 }
				 else if(arr4[l]==0){
					 goto label;
				 }
					 }m++;
				 }n++;}
				 
			 }
	  }
       
	}
	
}
label:
s1.inode[s1.ninode]=file_inode;
s1.ninode++;
lseek(fd,1024,SEEK_SET);
write(fd, &s1, 1024);
printf("%s file has been deleted\n",filename);
printf("New ninode value %d",s1.ninode);
}

#define TOKEN " "

int main(int args, char *arg[]){
char input[512];
char *input_command;
char *filepath;
char *parent_path;
char *dirname;
char *a, *b, *c;
char file_system_path;
char* from;
char* to;
int fd;
printf("Enter a command\n");
   while( 1 )
	{	
		scanf(" %[^\n]s", input);
      input_command = strtok(input," ");
		if(strcmp(input_command, "initfs")==0)
		{
			a = strtok(NULL, TOKEN);
			b = strtok(NULL, TOKEN);
			c = strtok(NULL, TOKEN);
         if (!a | !b | !c)
			{
				printf("enter proper arguments");
				continue;
			}
			else{
				filepath = a;
				int num_blck = atoi(b);
				int number_of_inodes = atoi(c);
            if( access(filepath, F_OK) == -1){
					fd = open(filepath, O_RDWR | O_CREAT , 0777);
					if(fd < 0)
					{
						printf("please enter proper path");
						continue;
					}
               else{			
					printf("initialized new file system that supports files upto 4GB\n");
					initfs(fd, num_blck, number_of_inodes);
               
               }
				}
			}
		}
      else if(strcmp(input_command, "q")==0){
      printf("saving and exiting\n");
      exit(0);
      }
	  else if(strcmp(input_command, "mkdir")==0){
		  a = strtok(NULL, TOKEN);
		  if (!a)
			{
				printf("enter proper arguments");
				continue;
			}
			else{
		  dirname=a;
	      mkdir(fd,dirname);
			}
	  }
	  else if(strcmp(input_command, "cpin")==0){
		a = strtok(NULL, TOKEN);
		b = strtok(NULL, TOKEN);
		if(!a | !b){
			printf("enter proper arguments");
				continue;
		}
		else{
			from=a;
			to=b;
			cpin(fd,from,to);
		}
		 
	  }
	  else if(strcmp(input_command, "cpout")==0){
		a = strtok(NULL, TOKEN);
		b = strtok(NULL, TOKEN);
		if(!a | !b){
			printf("enter proper arguments");
				continue;
		}
		else{
			from=a;
			to=b;
			cpout(fd,from,to);
		}
		 
	  }
	  else if(strcmp(input_command, "rm")==0){
		a = strtok(NULL, TOKEN);
		char *ptr;
        int ch='/';
        ptr = strrchr(a,ch);
        ptr++;
		if(!a){
			printf("enter proper arguments");
				continue;
		}
		else{
			rm(fd,ptr);
		}
		 
	  }

}
}