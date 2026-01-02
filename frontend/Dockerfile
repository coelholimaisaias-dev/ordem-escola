# Build stage
FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build -- --configuration production

# Serve stage
FROM nginx:alpine
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build /app/dist/frontend/browser/ .

# Corrige permissões
RUN chmod -R 755 .
RUN chown -R nginx:nginx .

# Banner opcional
RUN echo '\n===============================\n  OrdemEscola - Frontend Angular\n===============================\n' > /banner.txt

# SPA routing
COPY nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
