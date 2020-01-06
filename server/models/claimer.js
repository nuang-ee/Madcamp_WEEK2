const mongoose = require('mongoose');
const claimerSchema = new mongoose.Schema({
	claimer: String, // 보낼 상대 이름
	amount: Number, // 보낼 금액
	account: String, // 계좌 정보
	name: String, // 거래 이름
	date: String, // 거래 생성 날짜
	sent: { // 돈을 보냈는지 여부
		type: Boolean,
		default: false
	},
	received: { // 돈을 받았는지 여부
		type: Boolean,
		default: false
	}
})

module.exports = mongoose.model('claimer', claimerSchema)