const http = require('http');
const server = http.createServer((req, res) => {
  res.statusCode = 200;
  res.end('Hello, Node Multi-stage!');
});
server.listen(3000, () => console.log('Listening 3000'));
