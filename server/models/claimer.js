const mongoose = require('mongoose');
const claimerSchema = new mongoose.Schema({
	claimer_uid: {
		type: String,
		default: ""
	}, // 청구인 uid
	claimer: {
		type: String,
		default: ""
	}, // 청구인 이름
	amount: {
		type: Number,
		default: 0
	}, // 보낼 금액
	account: {
		type: String,
		default: ""
	}, // 계좌 정보
	name: {
		type: String,
		default: ""
	}, // 거래 이름
	date: {
		type: String,
		default: ""
	}, // 거래 생성 날짜
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