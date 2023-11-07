//package ru.netology.myapp.viewmodel
//
//import android.content.Intent
//import android.os.Bundle
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import ru.netology.myapp.FeedFragment.Companion.textArg
//import ru.netology.myapp.MediaPlayer.MediaLifecycleObserver
//import ru.netology.myapp.R
//import ru.netology.myapp.adapter.PostEventListener
//import ru.netology.myapp.dto.Post
//
//class ListenerImpl : PostEventListener {
////    (viewModel: PostViewModel)
//
//    override fun onLike(post: Post) {
//        if (post.likedByMe) {
//            viewModel.disLikeById(post.id)
//        } else {
//            viewModel.likeById(post.id)
//        }
//    }
//
//    override fun onShare(post: Post) {
//        val intent = Intent()
//            .setAction(Intent.ACTION_SEND)
//            .setType("text/plain")
//        val createChooser = Intent.createChooser(intent, "Choose app")
//        startActivity(createChooser)
//        viewModel.shareById(post.id)
//    }
//
//    override fun onRemove(post: Post) {
//        viewModel.removeById(post.id)
//    }
//
//    override fun onEdit(post: Post) {
//        findNavController().navigate(
//            R.id.action_feedFragment_to_editFragment,
//            Bundle().apply
//            { textArg = post.content }
//        )
//        viewModel.edit(post)
//    }
//
//    override fun onCancelEdit(post: Post) {
//        viewModel.edit(post)
//    }
//
//    override fun onShowOnePost(post: Post){
//        findNavController().navigate(
//            R.id.action_feedFragment_to_onePostFragment,
//            Bundle().apply
//            { textArg = post.id.toString() }
//        )
//    }
//
//    override fun onShowOneImage(post: Post) {
//        findNavController().navigate(
//            R.id.action_feedFragment_to_imageFragment,
//            Bundle().apply
//            { textArg = post.id.toString() }
//        )
//    }
//
//    override fun onShowVideoFragment(post: Post) {
//        findNavController().navigate(
//            R.id.action_feedFragment_to_videoFragment,
//            Bundle().apply
//            { textArg = post.id.toString() }
//        )
//    }
//
//    //
//    override fun onControlAudio(post: Post) {
//        var player = MediaLifecycleObserver()
//        val resours = post.attachment?.url
//        player.apply {
//            if (resours != null) {
//                mediaPlayer?.setDataSource(resours)
//            }
//        }.play()
//    }
//
//    override fun onShowUser(post: Post) {
//        findNavController().navigate(
//            R.id.action_feedFragment_to_userFragment,
//            Bundle().apply
//            { textArg = post.authorId.toString() }
//        )
//    }
//
//}
//}