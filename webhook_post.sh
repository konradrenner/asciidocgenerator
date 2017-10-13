#!/bin/sh
curl -vX POST http://localhost:8080/api/v1/generator/generatelocalFiles -d @localfolder_generation_template.json --header "Content-Type: application/json"
