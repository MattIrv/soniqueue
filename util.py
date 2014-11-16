import Party
import User
import Song

party_map = Party.party_map
user_map = User.user_map
email_map = User.email_map
song_map = Song.song_map

def create_party(u_id, party_name, party_location):
    try:
        new_party = Party(user_map[u_id], party_name, party_location)
        return new_party.jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})

def end(p_id):
    try:
        party_map[p_id].end()
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def next(p_id):
    try:
        next_song = party_map[p_id].next().top()
        return next_song.jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})

def nowplaying(p_id):
    try:
        cur_song = party_map[p_id].nowplaying
        return cur_song.jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})

def join(p_id, u_id):
    try:
        party_map[p_id].add_user(user_map[u_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_parties():
    try:
        return Party.jsonify_parties()
    except Exception as e:
        return json.dumps({'err': str(e)})

def leave(p_id, u_id):
    try:
        party_map[p_id].remove_user(user_map[u_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def login(email):
    try:
        if email not in email_map:
            User(email)
        return email_map[email].jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})

def add(u_id, spotify_id):
    try:
        cur_user = user_map[u_id]
        new_song = Song(spotify_id, cur_user)
        cur_user.queue.add_song(new_song)
        return new_song.jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})

def remove(u_id, s_id):
    try:
        user_map[u_id].queue.remove_song(song_map[s_id])
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def clear(u_id):
    try:
        user_map[u_id].queue.clear()        
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def move(u_id, s_id, disp):
    try:
        user_map[u_id].queue.move(song_map[s_id], disp)
        return json.dumps({})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_user_queue(u_id):
    try:
        user_queue = user_map[u_id].queue.list()
        return json.dumps({'userqueue': user_queue})
    except Exception as e:
        return json.dumps({'err': str(e)})

def list_party_queue(p_id):
    try:
        party_queue = party_map[p_id].queue.list()
        return json.dumps({'partyqueue': party_queue})
    except Exception as e:
        return json.dumps({'err': str(e)})

def user_info(u_id):
    try:
        return user_map[u_id].jsonify())
    except Exception as e:
        return json.dumps({'err': str(e)})

def set_alias(u_id, alias):
    try:
        cur_user = user_map[u_id]
        cur_user.set_alias(alias)
        return cur_user.jsonify()
    except Exception as e:
        return json.dumps({'err': str(e)})
