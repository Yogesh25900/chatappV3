package com.example.chatappv3

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zegocloud.zimkit.common.ZIMKitRouter
import com.zegocloud.zimkit.common.enums.ZIMKitConversationType
import com.zegocloud.zimkit.services.ZIMKit
import im.zego.zim.enums.ZIMErrorCode
import java.util.Arrays

class ChatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_chat)
      
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_items,menu)
        return true
            }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.newChat){
            showNewChatDialog()
            return true
        }
        if(item.itemId == R.id.newGroup){
            showNewGroupDialog()
            return true
        }
        if(item.itemId == R.id.joinGroup){
            showJoinGroupDialog()
            return true
        }
        return  super.onOptionsItemSelected(item)

    }

    private  fun showNewChatDialog(){
         val builder = AlertDialog.Builder(this)
        builder.setTitle("Start new chat")
        val targetUserEdit = EditText(this)
        targetUserEdit.hint ="Enter target User ID"
        builder.setView(targetUserEdit)

        builder.setPositiveButton("Start Chat"){_,_ ->
                val targetUserID =targetUserEdit.text.toString()
            startCHat(targetUserID,ZIMKitConversationType.ZIMKitConversationTypePeer)
        }

        builder.setNegativeButton("cancel",null)
        val dialog = builder.create()
        dialog.show()

    }

    private  fun startCHat(conversationID:String,type:ZIMKitConversationType){
        ZIMKitRouter.toMessageActivity(this,conversationID,type)
    }

    private fun showNewGroupDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Create New group")

        val gNameEdit =EditText(this)
        val gIDEdit =EditText(this)
        val gUserIDEdit = EditText(this)

        gNameEdit.hint ="Enter group name"
        gIDEdit.hint ="Enter group ID"
        gUserIDEdit.hint ="Invite User IDs"
         val layout = LinearLayout(this)
        layout.orientation =LinearLayout.VERTICAL
        layout.addView(gNameEdit)
        layout.addView(gIDEdit)
        layout.addView(gUserIDEdit)
        builder.setView(layout)

        builder.setPositiveButton("Create Group"){ _, _ ->
            val groupName = gNameEdit.text.toString()
            val groupID = gIDEdit.text.toString()
            val groupUserID = gUserIDEdit.text.toString().split(";".toRegex()).dropLastWhile  {it.isEmpty()}.toTypedArray()
            createGroup(groupName,groupID,ArrayList(Arrays.asList(*groupUserID)))
        }
            builder.setNegativeButton("Cancel",null)
        val dialog = builder.create()
        dialog.show()
    }

    private  fun createGroup(groupName:String,groupID:String,userIDs:List<String>){

        ZIMKit.createGroup(groupName,groupID,userIDs){
           groupInfo,inviteUserErrors,error ->
            if(error.code == ZIMErrorCode.SUCCESS){
                startCHat(groupInfo.id,ZIMKitConversationType.ZIMKitConversationTypeGroup)
            }
        }
    }
    private  fun showJoinGroupDialog(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Join Group")
        val joinGroupEdit = EditText(this)
        joinGroupEdit.hint = "enter group id"
        builder.setView(joinGroupEdit)

        builder.setPositiveButton("Join Group"){_,_ ->
            val groupID = joinGroupEdit.text.toString()
            joinGroup(groupID)
        }

        builder.setNegativeButton("Cancel",null)
        val dialog = builder.create()
        dialog.show()

    }

    private  fun joinGroup(groupID: String) {
        ZIMKit.joinGroup(groupID) { groupInfo, error ->
            if (error.code == ZIMErrorCode.SUCCESS || error.code == ZIMErrorCode.MEMBER_IS_ALREADY_IN_THE_GROUP) {
                startCHat(groupID, ZIMKitConversationType.ZIMKitConversationTypeGroup)
            }
        }
    }

     override fun onDestroy(){
        super.onDestroy()
        ZIMKit.disconnectUser()
    }


}