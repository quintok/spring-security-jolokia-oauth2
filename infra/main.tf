terraform {
  required_providers {
    azuread = {
      source  = "hashicorp/azuread"
      version = "2.26.1"
    }
  }
}

provider "azuread" {
}

data "azuread_client_config" "current" {}

resource "azuread_application" "example" {
  display_name     = "spring-oauth2-jolokia"
  owners           = [data.azuread_client_config.current.object_id]
  sign_in_audience = "AzureADMyOrg"
  identifier_uris  = [var.application_uri]
  api {
    mapped_claims_enabled          = true
    requested_access_token_version = 2

    oauth2_permission_scope {
      admin_consent_description  = "Allow users to access jolokia"
      admin_consent_display_name = "Access jolokia"
      enabled                    = true
      id                         = "ad101ca4-a4d9-4525-b25b-92b74a6cc3f3"
      type                       = "Admin"
      value                      = "jolokia_access"
    }
  }

  app_role {
    allowed_member_types = ["User"]
    description          = "Allow Jolokia Access"
    display_name         = "Jolokia Users"
    id                   = "25c73e5d-e0cb-46b6-889f-3b78591a8821"
    value                = "JolokiaUsers"
  }
}

resource "azuread_application_pre_authorized" "example" {
  application_object_id = azuread_application.example.object_id
  authorized_app_id     = "04b07795-8ddb-461a-bbee-02f9e1bf7b46"
  permission_ids        = [azuread_application.example.oauth2_permission_scope_ids["jolokia_access"]]
}



resource "azuread_service_principal" "this" {
  application_id               = azuread_application.example.application_id
  owners                       = [data.azuread_client_config.current.object_id]
  app_role_assignment_required = true
  feature_tags {
    enterprise = true
  }
}

resource "azuread_app_role_assignment" "example" {
  app_role_id         = azuread_application.example.app_role_ids["JolokiaUsers"]
  principal_object_id = data.azuread_client_config.current.object_id
  resource_object_id  = azuread_service_principal.this.object_id
}