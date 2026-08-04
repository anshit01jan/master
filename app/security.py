from hashlib import sha256
from secrets import token_urlsafe

from werkzeug.security import check_password_hash, generate_password_hash


def hash_password(password):
    return generate_password_hash(password)


def verify_password(password_hash, password):
    return check_password_hash(password_hash, password)


def generate_reset_token():
    return token_urlsafe(32)


def hash_reset_token(token):
    return sha256(token.encode("utf-8")).hexdigest()