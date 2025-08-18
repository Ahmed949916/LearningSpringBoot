const { createProxyMiddleware } = require('http-proxy-middleware');

module.exports = function (app) {
  app.use(
    [
      '/api',
      '/oauth2/authorization'  
    ],
    createProxyMiddleware({
      target: 'https://learningspringboot-production.up.railway.app',//http://localhost:8080 for local 
      changeOrigin: true,
      cookieDomainRewrite: 'localhost',
      xfwd: true,
      logLevel: 'silent',
    })
  );
};
