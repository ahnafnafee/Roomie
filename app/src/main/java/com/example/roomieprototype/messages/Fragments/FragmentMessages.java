package com.example.roomieprototype.messages.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomieprototype.R;
import com.example.roomieprototype.messages.Adapter.UserAdapter;
import com.example.roomieprototype.messages.Model.Chatlist;
import com.example.roomieprototype.messages.Model.User;
import com.example.roomieprototype.messages.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

public class FragmentMessages extends Fragment {


    public FragmentMessages() {
        // Required empty public constructor
    }

    private RecyclerView recyclerView;

    private UserAdapter userAdapter;
    private List<User> mUsers;

    private List<String> sUsers, cUsers;

    private ArrayList<String> matchList, matchEmailList, xUserList;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<Chatlist> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            matchList = bundle.getStringArrayList("matchList");
            matchEmailList = bundle.getStringArrayList("matchEmailList");
            Log.d("TAG", "fragmentmessages" + matchList.toString());
        }

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        usersList = new ArrayList<>();
        mUsers = new ArrayList<>();
        sUsers = new ArrayList<>();
        cUsers = new ArrayList<>();
        xUserList = new ArrayList<>();

        readUsers();

        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        Log.d("Outside ChatList", String.valueOf(reference));
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                Log.d("Outside ChatList", String.valueOf(usersList));
                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());


        return view;
    }

    private void readUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    final User tUser = childDataSnapshot.getValue(User.class);

                    if (!fuser.getUid().equals(tUser.getId())) {
                        xUserList.add(tUser.getId());
                    }

                    final DatabaseReference queryChatList = FirebaseDatabase.getInstance().getReference("roomieprototype").child("Chatlist");

                    queryChatList.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (final String item: xUserList) {
                                final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                                        .child(fuser.getUid())
                                        .child(item);

                                chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()){
                                            chatRef.child("id").setValue(item);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("Chatlist")
                                        .child(item)
                                        .child(fuser.getUid());
                                chatRefReceiver.child("id").setValue(fuser.getUid());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user!=null && user.getId() != null && !user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                            Log.d("Within ChatList: ", String.valueOf(user));
                            break;
                        }
                    }
                }


                Log.d("Inside ChatList", String.valueOf(mUsers));
                for (User u: mUsers) {
                    Log.d("Fullname", String.valueOf(u.getFullname()));
                    Log.d("Email", String.valueOf(u.getEmail()));
                    Log.d("ID", String.valueOf(u.getId()));
                    Log.d("Status", String.valueOf(u.getStatus()));
                    Log.d("IMG", String.valueOf(u.getImageURL()));
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}