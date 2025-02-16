FROM node:lts-alpine as build
WORKDIR /app
COPY package.json .
RUN npm install --force

FROM build as ng-build
COPY . .
RUN node_modules/.bin/ng build -c production

FROM nginx:1.25.5
COPY --from=ng-build /app/dist/**/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx.conf.d/default