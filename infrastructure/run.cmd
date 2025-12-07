docker-compose % docker-compose -f common.yml -f zookeeper.yml up -d

docker-compose % docker-compose -f common.yml -f kafka_cluster.yml up -d

docker-compose -f common.yml -f zookeeper.yml down

docker-compose -f common.yml -f kafka_cluster.yml down

docker-compose -f common.yml -f zookeeper.yml logs -f

docker-compose -f common.yml -f kafka_cluster.yml logs -f

