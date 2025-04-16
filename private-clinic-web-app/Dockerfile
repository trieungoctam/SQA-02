FROM node:20.12.0

WORKDIR /private-clinic-web-app/
COPY src/ /private-clinic-web-app/src
COPY public/ /private-clinic-web-app/public
COPY package.json /private-clinic-web-app/

RUN yarn install

EXPOSE 3000

CMD ["yarn", "start"]