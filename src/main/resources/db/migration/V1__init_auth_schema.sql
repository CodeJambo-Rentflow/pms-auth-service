-- V1__init_auth_schema.sql
-- Auth service initial schema per PMS-DB-001 §4

CREATE SCHEMA IF NOT EXISTS auth;

CREATE TABLE auth.user_profiles (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    keycloak_user_id    UUID NOT NULL UNIQUE,
    email               VARCHAR(255) NOT NULL UNIQUE,
    full_name           VARCHAR(255) NOT NULL,
    phone_encrypted     BYTEA,
    phone_hash          VARCHAR(64),
    role                VARCHAR(30) NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    mfa_enabled         BOOLEAN NOT NULL DEFAULT FALSE,
    last_login_at       TIMESTAMPTZ,
    created_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at          TIMESTAMPTZ
);

CREATE INDEX ix_user_profiles_email ON auth.user_profiles (email);
CREATE INDEX ix_user_profiles_keycloak ON auth.user_profiles (keycloak_user_id);
CREATE INDEX ix_user_profiles_phone_hash ON auth.user_profiles (phone_hash);
CREATE INDEX ix_user_profiles_role ON auth.user_profiles (role) WHERE deleted_at IS NULL;
