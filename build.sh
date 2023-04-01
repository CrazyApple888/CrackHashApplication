#!/bin/bash
docker build -t crackhash/manager -f docker/Dockerfile manager
docker build -t crackhash/worker -f docker/Dockerfile worker
