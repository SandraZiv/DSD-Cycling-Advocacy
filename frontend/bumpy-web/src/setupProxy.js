const proxy = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(
        proxy('/api/v1', { target: '127.0.0.1', changeOrigin: true })
    );
};
