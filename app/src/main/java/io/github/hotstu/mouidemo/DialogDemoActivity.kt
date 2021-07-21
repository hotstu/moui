package io.github.hotstu.mouidemo

import android.content.DialogInterface
import android.os.Bundle
import android.text.InputType
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import io.github.hotstu.moui.arch.MOSwipbackActivity
import io.github.hotstu.moui.dialog.MODialog
import io.github.hotstu.moui.dialog.MODialogAction

class DialogDemoActivity : MOSwipbackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.mo_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item?.itemId) {
            finish()
        }
        return true
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.button -> {
                //消息类型对话框（蓝色按钮）
                MODialog.MessageDialogBuilder(this)
                    .setTitle("标题")
                    .setMessage("确定要发送吗？")
                    .addAction("取消") { dialog, _ -> dialog.dismiss() }
                    .addAction("确定") { dialog, _ ->
                        dialog.dismiss()
                        Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }
            R.id.button2 -> {
                //消息类型对话框（红色按钮）
                MODialog.MessageDialogBuilder(this)
                    .setTitle("标题")
                    .setMessage("确定要删除吗？")
                    .addAction("取消") { dialog, _ -> dialog.dismiss() }
                    .addAction(0, "删除", MODialogAction.ACTION_PROP_NEGATIVE) { dialog, _ ->
                        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                    .show()
            }
            R.id.button3 -> {
                //消息类型对话框 (很长文案)
                MODialog.MessageDialogBuilder(this)
                    .setTitle("标题")
                    .setMessage(
                        "这是一段很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很" +
                                "长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长" +
                                "很长很长很长很长很长很长很长很长很长很长很长很长很长很长长很长的文案"
                    )
                    .addAction("取消") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            R.id.button4 -> {
                //菜单类型对话框
                val items = arrayOf("选项1", "选项2", "选项3")
                MODialog.MenuDialogBuilder(this)
                    .addItems(items, DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(this, "你选择了 " + items[which], Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                    .show()
            }
            R.id.button5 -> {
                //带 Checkbox 的消息确认框
                MODialog.CheckBoxMessageDialogBuilder(this)
                    .setTitle("退出后是否删除账号信息?")
                    .setMessage("删除账号信息")
                    .setChecked(true)
                    .addAction("取消") { dialog, index -> dialog.dismiss() }
                    .addAction("退出") { dialog, index -> dialog.dismiss() }
                    .show()
            }
            R.id.button6 -> {
                //单选菜单类型对话框
                val items = arrayOf("选项1", "选项2", "选项3")
                val checkedIndex = 1
                MODialog.CheckableDialogBuilder(this)
                    .setCheckedIndex(checkedIndex)
                    .addItems(items, DialogInterface.OnClickListener { dialog, which ->
                        Toast.makeText(this, "你选择了 " + items[which], Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    })
                    .show()
            }
            R.id.button7 -> {
                //多选菜单类型对话框
                val items = arrayOf("选项1", "选项2", "选项3", "选项4", "选项5", "选项6")
                val builder = MODialog.MultiCheckableDialogBuilder(this)
                    .setCheckedItems(intArrayOf(1, 3))
                    .addItems(items, DialogInterface.OnClickListener { dialog, which -> })
                builder.addAction("取消") { dialog, index -> dialog.dismiss() }
                builder.addAction("提交") { dialog, index ->
                    var result = "你选择了 "
                    for (i in 0 until builder.checkedItemIndexes.size) {
                        result += "" + builder.checkedItemIndexes[i] + "; "
                    }
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                builder.show()
            }
            R.id.button8 -> {
                //多选菜单类型对话框(item 数量很多)
                val items = arrayOf(
                    "选项1",
                    "选项2",
                    "选项3",
                    "选项4",
                    "选项5",
                    "选项6",
                    "选项7",
                    "选项8",
                    "选项9",
                    "选项10",
                    "选项11",
                    "选项12",
                    "选项13",
                    "选项14",
                    "选项15",
                    "选项16",
                    "选项17",
                    "选项18"
                )
                val builder = MODialog.MultiCheckableDialogBuilder(this)
                    .setCheckedItems(intArrayOf(1, 3))
                    .addItems(items, { dialog, which -> })
                builder.addAction("取消") { dialog, index -> dialog.dismiss() }
                builder.addAction("提交") { dialog, index ->
                    var result = "你选择了 "
                    for (i in 0 until builder.checkedItemIndexes.size) {
                        result += "" + builder.checkedItemIndexes[i] + "; "
                    }
                    Toast.makeText(this, result, Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                builder.show()
            }
            R.id.button9 -> {
                //带输入框的对话框
                val builder = MODialog.EditTextDialogBuilder(this)
                builder.setTitle("标题")
                    .setPlaceholder("在此输入您的昵称")
                    .setInputType(InputType.TYPE_CLASS_TEXT)
                    .addAction("取消") { dialog, index -> dialog.dismiss() }
                    .addAction("确定") { dialog, index ->
                        val text = builder.editText.text
                        if (text != null && text.isNotEmpty()) {
                            Toast.makeText(this, "您的昵称: " + text!!, Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        } else {
                            Toast.makeText(this, "请填入昵称", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .show()
            }
            R.id.button10 -> {
                //高度适应键盘升降的对话框
            }
            else -> {
            }
        }
    }
}
