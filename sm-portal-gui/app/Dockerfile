FROM node:12.22.7
WORKDIR /app
COPY package.json ./
RUN npm install
CMD ["npm", "run", "serve"]
EXPOSE 8080