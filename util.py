import Party
import User
import Song

party_map = Party.party_map
user_map = User.user_map
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

def login():
    pass

def add(u_id, spotify_id):
    return json.dumps(user_map[u_id].queue.add_song(Song(spotify_id)))

def remove(u_id, s_id):
    return json.dumps(user_map[u_id].queue.remove_song(song_map[s_id]))

def clear(u_id):
    return json.dumps(user_map[u_id].queue.clear())

def move(u_id, s_id, disp):
    return json.dumps(user_map[u_id].queue.move(song_map[s_id], disp))

def list_user_queue(u_id):
    return json.dumps(user_map[u_id].queue.list())

def list_party_queue(p_id):
    return json.dumps(party_map[p_id].queue.list())

def user_info(u_id):
    return json.dumps(user_map[u_id].info())

def set_alias(u_id, alias):
    return json.dumps(user_map[u_id].set_alias(alias))