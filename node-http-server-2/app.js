const http = require('http');
const os = require('os');

let app = http.createServer((req, res) => {

    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end('This is a response from ' + os.hostname + '\n');
});

const PORT = process.env.PORT || 3001;

app.listen(PORT, () => {
  console.log(`Server is listening on port ${PORT}`);
});
