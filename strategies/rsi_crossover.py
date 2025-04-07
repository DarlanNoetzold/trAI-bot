import requests
import numpy as np
import pandas as pd
import sys
import time

def fetch_candles(symbol="BTCUSDT", interval="5m", limit=100):
    url = f"http://localhost:8080/api/market/candles?symbol={symbol}&interval={interval}&limit={limit}"
    response = requests.get(url)
    return response.json()

def calculate_rsi(prices, period=14):
    delta = np.diff(prices)
    up = np.where(delta > 0, delta, 0)
    down = np.where(delta < 0, -delta, 0)

    roll_up = pd.Series(up).rolling(period).mean()
    roll_down = pd.Series(down).rolling(period).mean()

    rs = roll_up / roll_down
    rsi = 100.0 - (100.0 / (1.0 + rs))
    return rsi

def main():
    symbol = "BTCUSDT"
    while True:
        candles = fetch_candles(symbol=symbol)
        close_prices = [float(c[4]) for c in candles]

        rsi_series = calculate_rsi(close_prices)
        rsi = rsi_series.iloc[-1]

        print(f"[RSI] RSI atual para {symbol}: {rsi:.2f}")
        if rsi < 30:
            print("🔼 BUY sinal detectado.")
        elif rsi > 70:
            print("🔽 SELL sinal detectado.")
        else:
            print("⏸ HOLD - Nenhuma ação tomada.")

        time.sleep(60)

if __name__ == "__main__":
    main()