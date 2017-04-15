FROM vertx/vertx3-alpine
LABEL package=com.github.pjozsef.slack.emoji.tracker

ENV VERTICLE_HOME /usr/verticles
COPY build/libs $VERTICLE_HOME

ENV VERTX_OPTIONS "-conf /conf/config.json"

ARG MAIN_VERTICLE
ENV MAIN_VERTICLE $MAIN_VERTICLE
ENTRYPOINT ["sh", "-c"]
CMD ["sudo vertx run $MAIN_VERTICLE -cp $VERTICLE_HOME/* $VERTX_OPTIONS"]
