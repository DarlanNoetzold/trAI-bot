import pandas as pd
import requests

def get_closes(symbol="BTCUSDT", interval="1h", limit=100):
    url = f"https://api.binance.com/api/v3/klines?symbol={symbol}&interval={interval}&limit={limit}"
    raw_data = requests.get(url).json()
    closes = [float(entry[4]) for entry in raw_data]
    return pd.Series(closes)

def execute(symbol="BTCUSDT"):
    closes = get_closes(symbol)
    short_ma = closes.rolling(window=5).mean()
    long_ma = closes.rolling(window=20).mean()
    if short_ma.iloc[-2] < long_ma.iloc[-2] and short_ma.iloc[-1] > long_ma.iloc[-1]:
        print(f"[MA] Cruzamento de alta detectado em {symbol} -> SINAL DE COMPRA")
    elif short_ma.iloc[-2] > long_ma.iloc[-2] and short_ma.iloc[-1] < long_ma.iloc[-1]:
        print(f"[MA] Cruzamento de baixa detectado em {symbol} -> SINAL DE VENDA")
    else:
        print(f"[MA] Nenhum cruzamento detectado em {symbol}")

if __name__ == "__main__":
    execute()