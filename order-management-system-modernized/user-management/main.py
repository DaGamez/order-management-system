#!/usr/bin/env python3
"""
Main entry point for the User Management Service.

This script starts the FastAPI application and serves as the main entry point
for the User Management microservice.
"""

import os
import sys
from pathlib import Path

# Add the current directory to the Python path
current_dir = Path(__file__).parent
sys.path.insert(0, str(current_dir))

# Import the FastAPI app
from api import main

if __name__ == "__main__":
    main()
