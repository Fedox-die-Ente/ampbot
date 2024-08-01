use std::borrow::Cow;
use std::collections::HashMap;
use fluent_templates::{LanguageIdentifier, Loader, static_loader};
use fluent_templates::fluent_bundle::{FluentArgs, FluentValue};
use fluent_templates::fs::langid;

const US_ENGLISH: LanguageIdentifier = langid!("en-US");

static_loader! {
    static LOCALES = {
        locales: "locales",
        fallback_language: "en-US",
    };
}
fn main() {

    let mut args: HashMap<String, FluentValue> = HashMap::new();
    args.insert("name".to_string(), FluentValue::String(Cow::from("Fedox")));

    println!("{}", l(US_ENGLISH, "test2", &args));
}

pub fn l(language_identifier: LanguageIdentifier, text_id: &str, fluent_args: &HashMap<String, FluentValue>) -> String {
    if !fluent_args.is_empty() {
        LOCALES.lookup_with_args(&language_identifier, text_id, &fluent_args)
    } else {
        LOCALES.lookup(&language_identifier, text_id)
    }
}
