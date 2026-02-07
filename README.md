# thapo-poc
ThApo Proof of Concepts

## Run infra
```sh
cd devops
docker compose build
docker compose up -d --wait --remove-orphans --force-recreate
docker exec -it pocwebframeworks-iam /opt/keycloak/bin/kc.sh bootstrap-admin user --username admin --password adminpassword --no-prompt
```
