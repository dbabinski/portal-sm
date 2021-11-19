FROM node:12.22.7
WORKDIR /app
COPY package.json ./
RUN npm install
EXPOSE 8080
CMD ["npm","run","serve"]
