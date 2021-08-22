FROM itzg/minecraft-server:java8

ARG MOD_PATH
ARG MOD_JAR
ARG MC_VERSION
ARG FORGE_VERSION
 
RUN mkdir -m 777 /data/mods \
  && chown minecraft:minecraft /data/mods 
 
COPY $MOD_PATH/$MOD_JAR /data/mods/
 
RUN chmod 777 /data/mods/$MOD_JAR \
  && chown minecraft:minecraft /data/mods/$MOD_JAR
  
ENV NAME=mc
ENV EULA=TRUE
ENV VERSION=$MC_VERSION
ENV TYPE=FORGE
ENV FORGEVERSION=$FORGE_VERSION

COPY docker-log4j2.xml /tmp/log4j2.xml
