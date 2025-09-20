// generate-proxy.js
const fs = require('fs');
const environment = require('./src/environments/environment.json');

const proxy = {
  "/api": {
    target: "https://" + environment.domain + ":8443",
    secure: false,
    changeOrigin: true,
    potato: true
  },
  "/webauthn": {
    target: "https://" + environment.domain + ":8443",
    secure: false,
    changeOrigin: true
  }
};

fs.writeFileSync('proxy.conf.json', JSON.stringify(proxy, null, 2));
