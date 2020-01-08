## Endpoint 정리

##### client base url: http://34.84.158.57:4001/

### 1) Contact

---

1. <b>post </b> /contact/get <b> 이전에 DB에 저장했던 연락처 가져오기(localCached === true only)</b>

   - input: uid

   - output: contact list (아래는 output 예시)

     ```
     [
         {
             "localCached": true,
             "_id": "5e11c6aaf1e4da55aa0807a0",
             "name": "Minu",
             "phoneNumber": "1",
             "email": "kim-mins@naver.com",
             "thumbnail": "myImage-1578223274414.jpg"
         },
         {
             "localCached": true,
             "_id": "5e11c6aaf1e4da55aa0807a0",
             "name": "Minu",
             "phoneNumber": "1",
             "email": "kim-mins@naver.com",
             "thumbnail": "myImage-1578223274414.jpg"
         }
     ]
     ```

2) <b>post</b> /contact/add <b>연락처 추가하기</b>

   - input: uid, name, phoneNumber, email, localCached, thumbnail 이미지

   - output: \_id (추가된 연락처의 DB상의 id), result (아래는 예시)

     ```
     // 성공한 경우
     {
     	"_id": "5e11d138ae35c65adb683563",	// 추가된 연락처의 _id
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

3. <b>put</b> /contact/update <b>기존에 있던 연락처 수정</b>

   - input: uid, \_id, (name || phoneNumber || email || localCached || thumbnail) => 얘네는 필요하면 보내기. 서버 내에는 아래처럼 짜여있음(값이 있는 경우에만 처리하게끔)

     ```
     if (name) contact.name = name;
     if (phoneNumber) contact.phoneNumber = phoneNumber;
     if (email) contact.email = email;
     if (req.file.filename) contact.thumbnail = req.file.filename;
     if (localCached) contact.localCached = localCached;
     ```

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

4) <b>delete</b> /contact/delete <b>연락처 삭제</b>

   - input: uid, \_id

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

### 2) Image

---

1. <b>post </b> /image/get <b>기존에 DB에 저장돼있던 이미지 받아오기</b>

   - input: uid

   - output: image list (아래는 예시) // <b>"http://34.84.158.57:4001" + contentUrl로 접근</b>

     ```
     [
         {
             "localCached": true,
             "_id": "5e11de79359bcf5fc6336e72",
             "contentUrl": "/static/myImage-1578229369902.jpg"
         },
         {
             "localCached": true,
             "_id": "5e11de7b359bcf5fc6336e74",
             "contentUrl": "/static/myImage-1578229371065.jpg"
         },
     ]
     ```

2) <b>post</b> /image/add <b>이미지 추가</b>

   - input: uid, 추가할 이미지

   - output: \_id (추가된 이미지의 DB상의 id), result (아래는 예시)

     ```
     // 성공한 경우
     {
     	"_id": "5e11d138ae35c65adb683563",	// 추가한 이미지의 _id
         "result": 1
     }
     ```

     ```
     // (저장에) 실패한 경우
     {
         "result": 0
     }
     ```

3. <b>delete</b> /image/delete <b>이미지 삭제</b>

   - input: uid, \_id

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

### 3) User

---

1. <b>post</b> user/add <b>유저를 DB에 추가(최초 로그인 시에만 호출)</b>

   - input: uid, name

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

2) <b>post</b> user/check <b>유저가 DB에 있는지 확인</b>

   - input: uid

   - output: result (아래는 예시 <b>지금까지의 result랑은 좀 다름</b>)

     ```
     // user가 DB에 있는 경우
     {
         "result": 1
     }
     ```

     ```
     // user가 DB에 없는 경우
     {
         "result": 0
     }
     ```

3. ~~<b>delete</b> user/delete <b>유저를 DB에서 삭제</b>~~ // 필요하면 만들 예정

### 4) Claimer

---

1. <b>post</b> claimer/get <b>USER 본인이보내야 될 돈 목록을 가져옴</b>

   - input: uid

   - output: claimer list id (아래는 예시)

     ```
     [
         {
             "sent": false,
             "received": false,
             "_id": "5e14bcf0e7176d4e7096db57",
             "claimer": "김",
             "amount": 30403,
             "account": "111-1111-1111",
             "name": "coffffffeeeeee",
             "date": "11/22/33"
         }
     ]
     ```

2) <b>post</b> claimer/add <b>보내야 될 돈 추가</b>

   - input: uid, claimer(돈을 받을 사람의 이름), amount(금액), account(계좌 정보), name(이 거래의 이름), date(거래 생성 날짜)

   - output: \_id (추가된 보내야 될 돈들의 DB상의 id들), result (아래는 예시)

     ```
     {
         "_id": "5e13434b0a44e650bd12b3cc",
         "result": 1
     }
     ```

3. <b>put</b> claimer/sent <b>돈을 받은 경우, 받았다고 표시</b>

   - input: uid, \_id(보낸 claim의 DB상의 id)

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```

### 5) Claimee

---

1. <b>post</b> claimee/get <b>USER 본인이 보내야 될 돈 목록을 가져옴</b>

   - input: uid

   - output: claimee list (아래는 예시)

     ```
     [
     	{
             "received": false,
             "sent": false,
             "_id": "5e134d6d5d126755c9347a54",
             "claimee": "사람이름",
             "amount": 3280,
             "name": "coffee",
             "date": "44/44/44"
         },
         {
             "received": false,
             "sent": false,
             "_id": "5e14b8bde7176d4e7096db53",
             "claimee": "김",
             "amount": 30403,
             "name": "coffffffeeeeee",
             "date": "11/22/33"
         },
     ]
     ```

2) <b>post</b> claimee/add <b>보내야 될 돈 추가</b>

   - input: uid(본인), claimee(돈을 보내는 사람), amount(금액), name(이 거래의 이름), date(거래 생성 날짜)

   - output: result (아래는 예시)

     ```
     // 성공 시
     {
         "_id": "5e134cc781cbbb5542fd768d",
         "uid": "1750460951763668"	// 채무자의 uid
         "result": 1
     }
     ```

     ```
     // 실패 시
     {
     	result: 0
     }
     ```

3. <b>put</b> claimee/received <b>돈을 보낸 경우, 보냈다고 표시</b>

   - input: uid(돈 보낸 사람의 uid), \_id(해당 claim의 DB상의 id)

   - output: result (아래는 예시)

     ```
     // 성공한 경우
     {
         "result": 1
     }
     ```

     ```
     // 실패한 경우
     {
         "result": 0
     }
     ```
