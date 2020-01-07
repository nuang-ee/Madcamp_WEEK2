const mongoose = require('mongoose');
const claimeeSchema = new mongoose.Schema({
  claimee_uid: String, // 피청구인 uid
  claimee: String, // 피청구인 이름
  amount: Number, // 받을 금액
  name: String, // 거래 이름
  date: String, // 거래 생성 날짜
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