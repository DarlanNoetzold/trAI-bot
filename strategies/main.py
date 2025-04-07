import argparse
import subprocess
import sys
import os

def main():
    parser = argparse.ArgumentParser(description='Run crypto strategy bot')
    parser.add_argument('--strategy', required=True, help='Strategy name to run')
    parser.add_argument('--symbol', required=True, help='Symbol to analyze')
    parser.add_argument('--interval', required=False, help='Interval for candles')  # <-- AQUI

    args = parser.parse_args()

    strategy_script = os.path.join(os.path.dirname(__file__), f"{args.strategy}.py")

    if not os.path.exists(strategy_script):
        print(f"Strategy script '{args.strategy}.py' not found in strategies folder.")
        sys.exit(1)

    command = ['python', strategy_script, '--symbol', args.symbol]
    if args.interval:
        command += ['--interval', args.interval]

    print(f"Executing strategy: {args.strategy} for symbol {args.symbol}")
    print(f"Command: {' '.join(command)}")

    process = subprocess.Popen(
        command,
        stdout=subprocess.PIPE,
        stderr=subprocess.STDOUT,
        text=True
    )

    try:
        for line in process.stdout:
            print(line, end='')
    except KeyboardInterrupt:
        print("Stopping strategy execution.")
        process.terminate()

if __name__ == "__main__":
    main()
