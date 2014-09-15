echo [INFO] Start local h2 database by tcp.

cd `pwd`

java -cp ~/.m2/repository/com/h2database/h2/1.3.176/h2-1.3.176.jar org.h2.tools.Server -baseDir /Users/mac/wsmvn/abuye-parent/abuye-server-h2/tmp/h2db/
