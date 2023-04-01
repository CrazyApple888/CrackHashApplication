#!/bin/bash
docker build -t nsu/crackhashmanager -f docker/Dockerfile manager
docker build -t nsu/crackhashworker -f docker/Dockerfile worker