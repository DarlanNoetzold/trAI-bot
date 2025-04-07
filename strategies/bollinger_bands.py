import pandas as pd
import requests

def get_closes(symbol="BTCUSDT", interval="1h", limit=100):
    url = f"https://api.binance.com/api/v3/klines?symbol={symbol}&interval={interval}&limit={limit}"
    raw_data = requests.get(url).json()
    closes = [float(entry[4]) for entry in raw_data]
    return pd.Series(closes)

def execute(symbol="BTCUSDT"):
    closes = get_closes(symbol)
    ma = closes.rolling(window=20).mean()
    std = closes.rolling(window=20).std()
    upper = ma + (2 * std)
    lower = ma - (2 * std)

    last_price = closes.iloc[-1]
    if last_price > upper.iloc[-1]:
        print(f"[Bollinger] Preço acima da banda superior -> SINAL DE VENDA")
    elif last_price < lower.iloc[-1]:
        print(f"[Bollinger] Preço abaixo da banda inferior -> SINAL DE COMPRA")
    else:
        print(f"[Bollinger] Preço dentro das bandas -> Nenhuma ação")

if __name__ == "__main__":
    execute()