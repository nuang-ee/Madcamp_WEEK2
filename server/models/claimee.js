const mongoose = require('mongoose');
const claimeeSchema = new mongoose.Schema({
  claimee_uid: {
    type: String,
    default: ""
  }, // 피청구인 uid
  claimee: {
    type: String,
    default: ""
  }, // 피청구인 이름
  amount: {
    type: Number,
    default: 0
  }, // 받을 금액
  name: {
    type: String,
    default: ""
  }, // 거래 이름
  date: {
    type: String,
    default: ""
  }, // 거래 생성 날짜
  received: { // 돈을 받았는지 여부
    type: Boolean,
    default: false
  },
  sent: { // 돈을 보냈는지 여부
    type: Boolean,
    default: false
  }
})

module.exports = mongoose.model('claimee', claimeeSchema)