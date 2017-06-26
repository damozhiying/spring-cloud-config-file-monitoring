docker stop poc-gitblit
docker rename poc-gitblit poc-gitblit-old
docker run -d --name=poc-gitblit -p 8080:8080 -p 8443:8443 -p 9418:9418 -p 29418:29418 --volumes-from poc-gitblit-old springpoc/gitblit
docker rm -v poc-gitblit-old