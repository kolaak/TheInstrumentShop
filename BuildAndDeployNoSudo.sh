docker compose down 

mvn clean install

docker network create instrument_shop


docker compose build --build-arg DD_GIT_REPOSITORY_URL=github.com/shabuhabs/instrumentShop \
--build-arg DD_GIT_COMMIT_SHA=$(git rev-parse HEAD)

docker compose up -d
