#!/usr/bin/env python3

from swagger_server import app

def run():
    app.run(port=5000, debug=True)

if __name__ == '__main__':
    run()
