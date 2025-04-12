import argparse
import importlib
import requests
import sys

def get_candle_data(symbol, interval, limit=100):
    try:
        response = requests.get(
            f"http://localhost:8080/api/market/candles",
            params={"symbol": symbol, "interval": interval, "limit": limit},
            headers={"X-BINANCE-ENV": "TESTNET"}  # ou PRODUCTION conforme o front
        )
        response.raise_for_status()
        return response.json()
    except Exception as e:
        print(f"Erro ao obter dados da API: {e}")
        sys.exit(1)

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--strategy", required=True)
    parser.add_argument("--symbol", default="BTCUSDT")
    parser.add_argument("--interval", default="1m")
    args = parser.parse_args()

    print(f"Executando estratégia: {args.strategy}")
    candles = get_candle_data(args.symbol, args.interval)

    try:
        strategy_module = importlib.import_module(args.strategy)
        strategy_module.run(candles)
    except ModuleNotFoundError:
        print(f"Estratégia '{args.strategy}' não encontrada.")
    except AttributeError:
        print(f"Estratégia '{args.strategy}' não possui função 'run'.")

if __name__ == "__main__":
    main()
