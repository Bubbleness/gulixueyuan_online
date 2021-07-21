'use strict'
const merge = require('webpack-merge')
const prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
 // BASE_API: '"https://easy-mock.com/mock/5950a2419adc231f356a6636/vue-admin"',
 // nginx 地址 9001
 //BASE_API: '"http://localhost:9001"',
 
 // 现在使用网关地址  8222
 BASE_API: '"http://localhost:8222"',
})
