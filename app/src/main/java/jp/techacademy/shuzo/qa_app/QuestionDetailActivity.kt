package jp.techacademy.shuzo.qa_app

//import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.android.gms.common.util.WorkSourceUtil.add
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_question_detail.*
import java.util.HashMap
import com.google.firebase.database.ValueEventListener

class QuestionDetailActivity : AppCompatActivity() {
    private lateinit var mDatabaseReference: DatabaseReference
     private lateinit var mQuestion: Question
    private lateinit var mAdapter: QuestionDetailListAdapter
    private lateinit var mAnswerRef: DatabaseReference
    private lateinit var Reffavo: DatabaseReference
    private var fGenre=0
    private var mCurrentGenre:String=""
    private var SWQ:Int=0
    private var SWDQ:Int=0
    private var SWQKEY:String?=null

    private val mEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
          val map = dataSnapshot.value as Map<String, String>
            val answerUid = dataSnapshot.key ?: ""
            for (answer in mQuestion.answers) {
                // 同じAnswerUidのものが存在しているときは何もしない
                if (answerUid == answer.answerUid) {
                    return
                }
            }

            val body = map["body"] ?: ""
            val name = map["name"] ?: ""
            val uid = map["uid"] ?: ""
            val answer = Answer(body, name, uid, answerUid)
            mQuestion.answers.add(answer)
           mAdapter.notifyDataSetChanged()


        }
        fun ddValueEventListener(dataSnapshot: DataSnapshot) {
            //override fun onDataChange(dataSnapshot: DataSnapshot) {

            val map = dataSnapshot.value as Map<String, String>

                //textView.text = value.toString()

                }
         override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }


    private val fEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>



            val questionid = map["questionid"] ?: ""
            Log.d("MyLog at Word5555",questionid+"___"+mQuestion.questionUid)

            if(questionid==mQuestion.questionUid){
                Log.d("MyLog at Word3000",SWDQ.toString()+"-"+ SWQ.toString())
                SWQ=1
                SWDQ=1
                SWQKEY=dataSnapshot.key
                Log.d("MyLog at Word30001",SWDQ.toString()+"-"+ SWQ.toString()+" "+SWQKEY)

                val button: Button = findViewById<View>(R.id.favorites) as Button
                button.setBackgroundColor(Color.rgb(1, 192, 192))
          }

         }
        override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {

        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

        }

        override fun onCancelled(databaseError: DatabaseError) {

        }
    }









    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)
        val button: Button = findViewById<View>(R.id.favorites) as Button
        button.setBackgroundColor(Color.rgb(128, 128, 128))
        SWQ=0
        SWDQ=0


        // 渡ってきたQuestionのオブジェクトを保持する
        val extras = intent.extras
        mQuestion = extras.get("question") as Question
        mCurrentGenre = extras.get("genre").toString()


        if (mCurrentGenre=="5") {
            val btn: Button = findViewById(R.id.favorites);
            //btn.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.GONE);
        }



        title = mQuestion.title
        fGenre= mQuestion.genre

        Log.d("MyLog at Word3123","1"+fGenre.toString())
        Log.d("MyLog at Word3123","2")


        if (FirebaseAuth.getInstance().currentUser == null ) {
            val btn:Button = findViewById(R.id.favorites);
            //btn.setVisibility(View.INVISIBLE);
            btn.setVisibility(View.GONE);

        }else{
            mDatabaseReference = FirebaseDatabase.getInstance().reference
            Log.d("MyLog at Word3123", "3")
            val UID = FirebaseAuth.getInstance().currentUser!!.uid
            val genreRef = mDatabaseReference.child(FavoritesPATH).child(UID)
            Log.d("MyLog at Word3001", UID)
            genreRef.addChildEventListener(fEventListener)
        }
/*
        if (SWD==0){
            Log.d("MyLog at Word3001",8.toString() )
            val button: Button = findViewById<View>(R.id.favorites) as Button
            button.setBackgroundColor(Color.rgb(128, 128, 128))

        }
        else{
            val button: Button = findViewById<View>(R.id.favorites) as Button
            button.setBackgroundColor(Color.rgb(192, 192, 192))
            Log.d("MyLog at Word3001",8.toString() )
        }
*/

        Log.d("MyLog at Word3123",SWDQ.toString()+"-"+ SWQ.toString())



        // ListViewの準備
        mAdapter = QuestionDetailListAdapter(this, mQuestion)
        listView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        fab.setOnClickListener {
            // ログイン済みのユーザーを取得する
            val user = FirebaseAuth.getInstance().currentUser

            if (user == null) {
                // ログインしていなければログイン画面に遷移させる
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } else {
                // Questionを渡して回答作成画面を起動する
                // --- ここから ---
                val intent = Intent(applicationContext, AnswerSendActivity::class.java)
                intent.putExtra("question", mQuestion)
                startActivity(intent)
                // --- ここまで ---
            }

        }






            //Log.d("MyLog at Word3005",FAL.size.toString() )
       // fGenreRef = mDatabaseReference.child(ContentsPATH).child(mGenre.toString())
        //fGenreRef!!.addChildEventListener(mEventListener)




        favorites.setOnClickListener { v ->

            SWDQ=SWDQ+1
            if(SWDQ==2){SWDQ=0}

            Log.d("MyLog at Word3011",SWDQ.toString()+"-"+ SWQ.toString())

            if (SWDQ==0){

                val button: Button = findViewById<View>(R.id.favorites) as Button
                button.setBackgroundColor(Color.rgb(128, 128, 128))
            }else{
                val button: Button = findViewById<View>(R.id.favorites) as Button
                button.setBackgroundColor(Color.rgb(1, 192, 192))
            }

    }







        val dataBaseReference = FirebaseDatabase.getInstance().reference
        mAnswerRef = dataBaseReference.child(ContentsPATH).child(mQuestion.genre.toString()).child(mQuestion.questionUid).child(AnswersPATH)
        mAnswerRef.addChildEventListener(mEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()


        if (FirebaseAuth.getInstance().currentUser != null) {
            val UID = FirebaseAuth.getInstance().currentUser!!.uid
            val genreRef = mDatabaseReference.child(FavoritesPATH).child(UID)
            val data = HashMap<String, String>()

            Log.d("MyLog at Word3011", SWDQ.toString() + SWQ.toString())



            if (SWDQ == 0) {
                if (SWQ == 1) {
                    //   data["questionid"] = ""
                    //   Log.d("MyLog at Word3011",mQuestion.questionUid )
                    //   Log.d("MyLog at Word3021",mQuestion.questionUid+" "+SWQKEY )
                    //   genreRef.push().setValue(data)
                    data["questionid"] = ""
                    val genreRef =
                        mDatabaseReference.child(FavoritesPATH).child(UID).child(SWQKEY.toString())
                    Log.d("MyLog at Word3021", mQuestion.questionUid + " " + SWQKEY)
                    genreRef.setValue(null)


                }
            } else if (SWQ == 0) {

                /*
     val genreRef = mDatabaseReference.child(FavoritesPATH).child(UID).child(SWQKEY.toString())
     Log.d("MyLog at Word3021",mQuestion.questionUid+" "+SWQKEY )
     genreRef.push().setValue(null)
    */
                data["questionid"] = mQuestion.questionUid
                data["genre"] = fGenre.toString()
                Log.d("MyLog at Word3011", mQuestion.questionUid)
                Log.d("MyLog at Word3021", mQuestion.questionUid + " " + SWQKEY)
                genreRef.push().setValue(data)


            }
        }


    }





}