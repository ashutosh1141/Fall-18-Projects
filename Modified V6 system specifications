Ashutosh Agrawala axa180037



steps to compile the code:
1.use the command  cc -std=c99 fsaccess.c

specifications:
1.super block of size 1023 bytes.
2.inode structure of size 64 bytes.
3.free array of size 150.
4.inode array of size 100;
5.file size limited to 4gb by making the size of 4bytes using short for each of the low and high bytes of the size0 and size1 variable

Methods implemented:
1.initfs-To initialize the V6 file system.
  -calculate the isize
  -calculate the number of available data blocks
  -calculate the number of used data blocks
  -set the field of the superblock
  Other methods used inside initfs:
  (i) chaining of the free array:
      (A) The first set of 150 logical blocks that are free are strored in the super blocks free array.
      (B) The next set of 150 blocks are copied into a struct of free array conataining the nfree value and free array logical block numbers.
      (C) The freearray struct is then written to the logical block pointed by free[0] of the super block to maintain thye chaining.
  (ii)initializing the root directory inode:
      (A) inode 1 is allocated to the root directory.
      (B) The addr array of the inode corresponding to the root directory is made to point to the first data block(blocks after that have been added to the free array).
      (C) The flag field is made 140777.1 corresponds that the inode is allocated,4 corresponds that the type is a directory and 777 are the read,write and excecute permissions of the file.
      (D) Then we make a directory structure to store the parent directory info and the info about the directory itself with the corresponding inode numbers in the first 2 bytes and the filename in the next 14 bytes.
      (E) We then copy the directory structures content to the logical data block corresponding to the root directory which is the first data block that we have allocated.
  (iii)Printing a set of chained free array data blocks:
      (A) we are printing the set of next available free blocks.
      (B) this is done by using lseek() and moving to the 1st data block after the one allocated for the root directory and printing its contents.
2. q-whenever a user enters q we exit the filesystem by saving its contents.As the file contents are already written during the initfs we do not need to write again,just an exit(0) is enough.
3.cpin-to copy contents of external file to a v6 file
  -calculate the size of external file
  -decide whether it falls in a small file category or large file category
  -allocate inode for the v6 file
  -set all the fields of the inode fields
  -construct the addr array depending on the file size
  -write the inode contents to the allocated inode location
  -write the filename in the parent directory of the file
4.cpout-to copy contents of v6 file to an external file
  -determine the size of the internal v6 file
  -read the inode from the inode number of the v6 file
  -if the file is small then read the contents of the addr array block by block and write to external file
  -if the file is large then copy the contents of the file looping through the direct blocks first and then the triple indirect block
5.mkdir-make a new directory in the v6 file system
  -We are first finding the next available inode from the inode array of superblock.
  -After finding the inode no, we are writing inode no and directory name in the root directory data block.
  -After adding the directory entry, we are allocating next available data block from the free array of superblock.
  -In this datablock, we are adding two entries with . and ..  
6.Rm-remove a v6 file from the system
  -Since the file resides in the root directory whose Inode no is 1, we are fetching the contents of Inode1.
  -After fetching the contents from Inode1, we use address array of the inode to find the data block numbers.
  -After finding the data block numbers, We are scanning all data blocks to find the entry of required file.
  -After finding the file entry, we read the inode no of the file.
  -We are then fetching the contents of that inode to find the data blocks occupied by the file.
  -After finding the data block numbers, we are adding these blocks in the free array and increasing the nfree value accordingly.
  -After freeing data blocks, we are adding the inode to inode array and increasing the ninode value accordingly.
  -To validate the method we have written, we are printing the inode count before and after the method operation

 
 
