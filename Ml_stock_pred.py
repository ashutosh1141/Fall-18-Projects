import pandas as pd
import quandl

df=pd.read_csv('tickers.csv')
print(df)

df_ = pd.DataFrame()

startdate = "2000-1-1"
enddate = "2018-10-1"

for i in df.Ticker:
	quandl.ApiConfig.api_key='R47pEb6xhJTpDbWaidA3'
	data = quandl.get_table('WIKI/PRICES', ticker = i, qopts = { 'columns': ['ticker', 'date', 'adj_close'] }, date = { 'gte': startdate, 'lte': enddate }, paginate=True)
	data.fillna(method='ffill')
	data.fillna(method='bfill')
	df_.append(data)