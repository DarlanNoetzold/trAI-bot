import pandas as pd
import requests

def get_klines(symbol="BTCUSDT", interval="1h", limit=100):
    url = f"https://api.binance.com/api/v3/klines?symbol={symbol}&interval={interval}&limit={limit}"
    raw_data = requests.get(url).json()
    closes = [float(entry[4]) for entry in raw_data]
    return closes

def compute_rsi(closes, period=14):
    series = pd.Series(closes)
    delta = series.diff()
    gain = (delta.where(delta > 0, 0)).rolling(window=period).mean()
    loss = (-delta.where(delta < 0, 0)).rolling(window=period).mean()
    rs = gain / loss
    rsi = 100 - (100 / (1 + rs))
    return rsi

def execute(symbol="BTCUSDT"):
    closes = get_klines(symbol)
    rsi = compute_rsi(closes)
    last_rsi = rsi.iloc[-1]
    print(f"[RSI] Último RSI: {last_rsi:.2f}")
    if last_rsi < 30:
        print(f"[RSI] Sinal de COMPRA para {symbol}")
    elif last_rsi > 70:
        print(f"[RSI] Sinal de VENDA para {symbol}")
    else:
        print(f"[RSI] Nenhuma ação para {symbol}")

if __name__ == "__main__":
    execute()