import requests
import pandas as pd
import time

def fetch_candles(symbol="BTCUSDT", interval="5m", limit=100):
    url = f"http://localhost:8080/api/market/candles?symbol={symbol}&interval={interval}&limit={limit}"
    response = requests.get(url)
    return response.json()

def main():
    symbol = "BTCUSDT"
    while True:
        candles = fetch_candles(symbol=symbol)
        close_prices = [float(c[4]) for c in candles]
        df = pd.DataFrame(close_prices, columns=["close"])

        df["ema_short"] = df["close"].ewm(span=5, adjust=False).mean()
        df["ema_long"] = df["close"].ewm(span=20, adjust=False).mean()

        if df["ema_short"].iloc[-1] > df["ema_long"].iloc[-1] and df["ema_short"].iloc[-2] <= df["ema_long"].iloc[-2]:
            print("🔼 BUY sinal EMA crossover detectado.")
        elif df["ema_short"].iloc[-1] < df["ema_long"].iloc[-1] and df["ema_short"].iloc[-2] >= df["ema_long"].iloc[-2]:
            print("🔽 SELL sinal EMA crossover detectado.")
        else:
            print("⏸ HOLD - Nenhuma ação tomada.")
        time.sleep(60)

if __name__ == "__main__":
    main()