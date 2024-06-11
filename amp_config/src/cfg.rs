use serde::{Deserialize, Serialize};

#[derive(Serialize, Deserialize, Debug)]
pub struct BotConfig {
    pub database: DatabaseConfig,

    pub tokens: Tokens,
}

#[derive(Serialize, Deserialize, Debug)]
pub struct DatabaseConfig {
    pub host:     DatabaseConnection,
    pub user:     String,
    pub password: String,
    pub database: String,
}

#[derive(Serialize, Deserialize, Debug)]
#[serde(untagged)]
pub enum DatabaseConnection {
    Tcp(String, u16),
    Unix(String),
}

#[derive(Serialize, Deserialize, Debug)]
pub struct Tokens {
    pub discord: String
}

