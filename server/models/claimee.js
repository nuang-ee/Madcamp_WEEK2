const mongoose = require ('mongoose');
const claimeeSchema = new mongoose.Schema({
  claimer: String,  // 보낼 상대 이름
  amount: Number,   // 보낼 금액
  account: String,  // 계좌 정보
  name: String,     // 거래 이름
  date: String      // 거래 생성 날짜
})

module.exports = mongoose.model('claimee', claimeeSchema)