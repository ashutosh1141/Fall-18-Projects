####################
####Author: Ashutosh Agrawala####

import os
import numpy as np
import pandas as pd
from pandas_datareader import data
from datetime import date
import quandl
import math
from sklearn import preprocessing
from sklearn.model_selection import train_test_split
from sklearn import metrics
from sklearn.neighbors import KNeighborsClassifier
from sklearn.ensemble import RandomForestClassifier

#os.chdir('Documents/Acad - Masters/Fall 2018/Sys - Computation Finance/Homeworks/Midterm Project 2')

def SMA(prices, window):
	sma = prices.cumsum()
	sma.values[window:] = (sma.values[window:] - sma.values[:-window])/window
	sma.iloc[:window] = np.nan
	return prices/sma

def EMA(prices,window):
	ema = prices.copy()
	alpha = 2/(1+window)

	for day in range(len(prices)):
		if day < window:
			ema[day] = np.nan
		elif day == window:
			ema[day] = prices.cumsum()[day-1]/window
		elif day > window:
			ema[day] = prices[day]*alpha + ema[day-1]*(1-alpha)

	return prices/ema

def MACD(prices):
	macd_diff = EMA(prices,26) - EMA(prices,13)
	macd_diff = macd_diff.fillna(0)
	macd_denom = EMA(macd_diff,9)
	return macd_diff/macd_denom

tickers = pd.read_csv('tickers.csv')
tickers = tickers['Ticker'].tolist()

startdate = "2000-1-1"
enddate = "2018-10-1"

stockdata = pd.DataFrame()
print(len(tickers))
count = 1

for ticker in tickers:
	try:
		quandl.ApiConfig.api_key = 'WfwECWaM6z39jHjpMDmA'
		data = quandl.get_table('WIKI/PRICES', ticker = ticker, 
			qopts = { 'columns': ['ticker', 'date', 'adj_close'] }, 
			date = { 'gte': startdate, 'lte': enddate }, paginate=True)
		data = data.fillna(method = 'ffill')
		data = data.fillna(method = 'bfill')
		x = data[['adj_close']].values.astype(float)
		min_max_scaler = preprocessing.MinMaxScaler()
		x_scaled = min_max_scaler.fit_transform(x)
		data[['adj_close']] = pd.DataFrame(x_scaled)
		data['SMA_26'] = SMA(data['adj_close'],26)
		data['SMA_10'] = SMA(data['adj_close'],10)
		data['SMA_5'] = SMA(data['adj_close'],5)
		data['EMA_26'] = EMA(data['adj_close'],26)
		data['EMA_10'] = EMA(data['adj_close'],10)
		data['EMA_5'] = EMA(data['adj_close'],5)
		data['MACD'] = MACD(data['adj_close'])
		data['Next Price'] = data['adj_close'].shift(-1)
		stockdata = stockdata.append(data)

		print(count)
		count += 1
		
	except:
		continue

stockdata = stockdata.dropna()

feature_cols = ['SMA_26','SMA_10','SMA_5','EMA_26','EMA_10','EMA_5','MACD']
X = stockdata[feature_cols]
Y = np.where(stockdata['Next Price'] > stockdata['adj_close'],1,0)
X_train,X_test,Y_train,Y_test=train_test_split(X,Y,test_size=0.4)

model_KNN = KNeighborsClassifier(n_neighbors=3)
model_KNN.fit(X_train,Y_train)

model_RF = RandomForestClassifier(n_estimators=100)
model_RF.fit(X_train,Y_train)

Y_pred_KNN = model_KNN.predict(X_test)
accuracy_KNN = metrics.accuracy_score(Y_test,Y_pred_KNN)
precision_KNN = metrics.precision_score(Y_test,Y_pred_KNN)
auc_KNN = metrics.roc_auc_score(Y_test,Y_pred_KNN)

Y_pred_RF = model_RF.predict(X_test)
accuracy_RF = metrics.accuracy_score(Y_test,Y_pred_RF)
precision_RF = metrics.precision_score(Y_test,Y_pred_RF)
auc_RF = metrics.roc_auc_score(Y_test,Y_pred_RF)

print("Accuracy results for KNN model on given 10 tickers: ")
print("Accuracy: ", accuracy_KNN)
print("Precision: ", precision_KNN)
print("ROC - AUC value: ", auc_KNN)
print()

print("Accuracy results for Random Forest Classifier model on given 10 tickers: ")
print("Accuracy: ", accuracy_RF)
print("Precision: ", precision_RF)
print("ROC - AUC value: ", auc_RF)



nasd = pd.read_csv('tickers_nasd.csv')
nyse = pd.read_csv('tickers_nyse.csv')
total = pd.concat([nasd,nyse])

total['MarketCap'] = total['MarketCap'].str[1:]

total.MarketCap = (total.MarketCap.replace(r'[MB]+$', '', regex=True).astype(float) * \
                    total.MarketCap.str.extract(r'[\d\.]+([MB]+)', expand=False)
                    .fillna(1)
                    .replace(['M','B'], [10**6, 10**9]).astype(int))


total_500M = total[(total.MarketCap >= 500000000)]

tickers2 = set(total_500M.Symbol)
print()
print(len(tickers))

accuracytable = []
count = 1

for ticker in tickers2:
	try:
		quandl.ApiConfig.api_key = 'WfwECWaM6z39jHjpMDmA'
		data = quandl.get_table('WIKI/PRICES', ticker = ticker, 
			qopts = { 'columns': ['ticker', 'date', 'adj_close'] }, 
			date = { 'gte': startdate, 'lte': enddate }, paginate=True)
		data = data.fillna(method = 'ffill')
		data = data.fillna(method = 'bfill')
		x = data[['adj_close']].values.astype(float)
		min_max_scaler = preprocessing.MinMaxScaler()
		x_scaled = min_max_scaler.fit_transform(x)
		data[['adj_close']] = pd.DataFrame(x_scaled)
		data['SMA_26'] = SMA(data['adj_close'],26)
		data['SMA_10'] = SMA(data['adj_close'],10)
		data['SMA_5'] = SMA(data['adj_close'],5)
		data['EMA_26'] = EMA(data['adj_close'],26)
		data['EMA_10'] = EMA(data['adj_close'],10)
		data['EMA_5'] = EMA(data['adj_close'],5)
		data['MACD'] = MACD(data['adj_close'])
		data['Next Price'] = data['adj_close'].shift(-1)
		data = data.dropna()
		feature_cols = ['SMA_26','SMA_10','SMA_5','EMA_26','EMA_10','EMA_5','MACD']
		X_test = data[feature_cols]
		Y_test = np.where(data['Next Price'] > data['adj_close'],1,0)
		
		Y_pred_KNN = model_KNN.predict(X_test)
		accuracy_KNN = metrics.accuracy_score(Y_test,Y_pred_KNN)
		precision_KNN = metrics.precision_score(Y_test,Y_pred_KNN)
		auc_KNN = metrics.roc_auc_score(Y_test,Y_pred_KNN)


		Y_pred_RF = model_RF.predict(X_test)
		accuracy_RF = metrics.accuracy_score(Y_test,Y_pred_RF)
		precision_RF = metrics.precision_score(Y_test,Y_pred_RF)
		auc_RF = metrics.roc_auc_score(Y_test,Y_pred_RF)

		accuracytable.append([ticker,"K-nearest neighbor",accuracy_KNN,precision_KNN,auc_KNN])
		accuracytable.append([ticker,"Random Forest Classifier",accuracy_RF,precision_RF,auc_RF])

		print(count)
		count += 1
			
	except:
		continue

accuracytable = pd.DataFrame(accuracytable)
accuracytable.columns = ['Ticker','Model','Accuracy Value','Precision Value','ROC - AUC value']

accuracytable.to_csv('Accuracy Results.csv')

for ticker in tickers:
	accuracytable = accuracytable[accuracytable.Ticker != ticker]

tempdata_KNN = accuracytable[accuracytable.Model == "K-nearest neighbor"]
tempdata_KNN = tempdata_KNN.sort_values(by=['Accuracy Value'], ascending=False)

tempdata_RF = accuracytable[accuracytable.Model == "Random Forest Classifier"]
tempdata_RF = tempdata_RF.sort_values(by=['Accuracy Value'], ascending=False)

finalstocks_KNN = tempdata_KNN[:20]
finalstocks_RF = tempdata_RF[:20]

finalstocks_KNN.to_csv("Final 20 stocks - KNN.csv")
finalstocks_RF.to_csv("Final 20 stocks - RF.csv")
