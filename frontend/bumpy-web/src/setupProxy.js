const proxy = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        proxy('/api/v1', { target: 'http://161.53.67.132:5000', changeOrigin: true })
    );
};
