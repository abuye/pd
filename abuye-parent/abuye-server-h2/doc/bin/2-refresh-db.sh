#!/bin/bash
echo [INFO] Re-create the schema and provision the sample data.

cd `pwd`
cd ../../

mvn antrun:run -X

cd doc/bin
pause