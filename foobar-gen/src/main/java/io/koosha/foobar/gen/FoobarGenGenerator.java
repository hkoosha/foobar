package io.koosha.foobar.gen;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openapitools.codegen.*;
import org.openapitools.codegen.languages.AbstractJavaCodegen;
import org.openapitools.codegen.languages.features.BeanValidationFeatures;
import org.openapitools.codegen.languages.features.GzipFeatures;
import org.openapitools.codegen.languages.features.PerformBeanValidationFeatures;
import org.openapitools.codegen.meta.features.DocumentationFeature;
import org.openapitools.codegen.meta.features.GlobalFeature;
import org.openapitools.codegen.model.ModelMap;
import org.openapitools.codegen.model.ModelsMap;
import org.openapitools.codegen.model.OperationMap;
import org.openapitools.codegen.model.OperationsMap;
import org.openapitools.codegen.utils.ProcessUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.openapitools.codegen.utils.StringUtils.camelize;

@SuppressWarnings("SpellCheckingInspection")
public class FoobarGenGenerator extends AbstractJavaCodegen
        implements BeanValidationFeatures, PerformBeanValidationFeatures, GzipFeatures {

    private final Logger LOGGER = LoggerFactory.getLogger(FoobarGenGenerator.class);

    public static final String ASYNC_NATIVE = "asyncNative";
    public static final String CONFIG_KEY = "configKey";
    public static final String PARCELABLE_MODEL = "parcelableModel";
    public static final String USE_RUNTIME_EXCEPTION = "useRuntimeException";
    public static final String USE_REFLECTION_EQUALS_HASHCODE = "useReflectionEqualsHashCode";
    public static final String USE_ABSTRACTION_FOR_FILES = "useAbstractionForFiles";
    public static final String DYNAMIC_OPERATIONS = "dynamicOperations";
    public static final String SUPPORT_STREAMING = "supportStreaming";
    public static final String GRADLE_PROPERTIES = "gradleProperties";
    public static final String ERROR_OBJECT_TYPE = "errorObjectType";

    public static final String FEIGN = "feign";

    public static final String SERIALIZATION_LIBRARY_GSON = "gson";
    public static final String SERIALIZATION_LIBRARY_JACKSON = "jackson";
    public static final String SERIALIZATION_LIBRARY_JSONB = "jsonb";

    protected String gradleWrapperPackage = "gradle.wrapper";
    protected String configKey = null;

    protected boolean asyncNative = false;
    protected boolean parcelableModel = false;
    protected boolean useBeanValidation = false;
    protected boolean performBeanValidation = false;
    protected boolean useGzipFeature = false;
    protected boolean useRuntimeException = false;
    protected boolean useReflectionEqualsHashCode = false;
    protected boolean useAbstractionForFiles = false;
    protected boolean dynamicOperations = false;
    protected boolean supportStreaming = false;
    protected String gradleProperties;
    protected String errorObjectType;
    protected String authFolder;
    protected boolean useOneOfDiscriminatorLookup = false; // use oneOf discriminator's mapping for model lookup
    protected boolean useSingleRequestParameter = false;

    public FoobarGenGenerator() {
        super();

        // TODO: Move GlobalFeature.ParameterizedServer to library: jersey after moving featureSet to generatorMetadata
        modifyFeatureSet(features -> features
                .includeDocumentationFeatures(DocumentationFeature.Readme)
                .includeGlobalFeatures(GlobalFeature.ParameterizedServer)
        );

        outputFolder = "generated-code" + File.separator + "java";
        embeddedTemplateDir = templateDir = "foobar-gen";
        invokerPackage = "org.openapitools.client";
        artifactId = "openapi-java-client";
        apiPackage = "org.openapitools.client.api";
        modelPackage = "org.openapitools.client.model";

        // cliOptions default redefinition need to be updated
        updateOption(CodegenConstants.INVOKER_PACKAGE, this.getInvokerPackage());
        updateOption(CodegenConstants.ARTIFACT_ID, this.getArtifactId());
        updateOption(CodegenConstants.API_PACKAGE, apiPackage);
        updateOption(CodegenConstants.MODEL_PACKAGE, modelPackage);

        modelTestTemplateFiles.put("model_test.mustache", ".java");

        cliOptions.add(CliOption.newBoolean(PARCELABLE_MODEL, "Whether to generate models for Android that implement Parcelable with the okhttp-gson library."));
        cliOptions.add(CliOption.newBoolean(USE_BEANVALIDATION, "Use BeanValidation API annotations"));
        cliOptions.add(CliOption.newBoolean(PERFORM_BEANVALIDATION, "Perform BeanValidation"));
        cliOptions.add(CliOption.newBoolean(USE_GZIP_FEATURE, "Send gzip-encoded requests"));
        cliOptions.add(CliOption.newBoolean(USE_RUNTIME_EXCEPTION, "Use RuntimeException instead of Exception"));
        cliOptions.add(CliOption.newBoolean(ASYNC_NATIVE, "If true, async handlers will be used, instead of the sync version"));
        cliOptions.add(CliOption.newBoolean(USE_REFLECTION_EQUALS_HASHCODE, "Use org.apache.commons.lang3.builder for equals and hashCode in the models. WARNING: This will fail under a security manager, unless the appropriate permissions are set up correctly and also there's potential performance impact."));
        cliOptions.add(CliOption.newBoolean(USE_ABSTRACTION_FOR_FILES, "Use alternative types instead of java.io.File to allow passing bytes without a file on disk. Available on resttemplate, webclient, libraries"));
        cliOptions.add(CliOption.newBoolean(DYNAMIC_OPERATIONS, "Generate operations dynamically at runtime from an OAS", this.dynamicOperations));
        cliOptions.add(CliOption.newBoolean(SUPPORT_STREAMING, "Support streaming endpoint (beta)", this.supportStreaming));
        cliOptions.add(CliOption.newString(GRADLE_PROPERTIES, "Append additional Gradle properties to the gradle.properties file"));
        cliOptions.add(CliOption.newString(ERROR_OBJECT_TYPE, "Error Object type. (This option is for okhttp-gson-next-gen only)"));
        cliOptions.add(CliOption.newString(CONFIG_KEY, "Config key in @RegisterRestClient. Default to none. Only `microprofile` supports this option."));
        cliOptions.add(CliOption.newBoolean(CodegenConstants.USE_ONEOF_DISCRIMINATOR_LOOKUP, CodegenConstants.USE_ONEOF_DISCRIMINATOR_LOOKUP_DESC + " Only jersey2, jersey3, native, okhttp-gson support this option."));
        cliOptions.add(CliOption.newBoolean(CodegenConstants.USE_SINGLE_REQUEST_PARAMETER, "Setting this property to true will generate functions with a single argument containing all API endpoint parameters instead of one argument per parameter. ONLY jersey2, jersey3, okhttp-gson support this option."));

        supportedLibraries.put(FEIGN, "HTTP client: OpenFeign 10.x. JSON processing: Jackson 2.9.x.");

        CliOption libraryOption = new CliOption(CodegenConstants.LIBRARY, "library template (sub-template) to use");
        libraryOption.setEnum(supportedLibraries);

        CliOption serializationLibrary = new CliOption(CodegenConstants.SERIALIZATION_LIBRARY, "Serialization library, default depends on value of the option library");
        Map<String, String> serializationOptions = new HashMap<>();
        serializationOptions.put(SERIALIZATION_LIBRARY_JACKSON, "Use Jackson as serialization library");
        serializationLibrary.setEnum(serializationOptions);
        cliOptions.add(serializationLibrary);

        // Ensure the OAS 3.x discriminator mappings include any descendent schemas that allOf
        // inherit from self, any oneOf schemas, any anyOf schemas, any x-discriminator-values,
        // and the discriminator mapping schemas in the OAS document.
        this.setLegacyDiscriminatorBehavior(false);
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.CLIENT;
    }

    @Override
    public String getName() {
        return "java";
    }

    @Override
    public String getHelp() {
        return "Generates a Feign Java client library for Foobar project.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(CodegenConstants.USE_ONEOF_DISCRIMINATOR_LOOKUP)) {
            setUseOneOfDiscriminatorLookup(convertPropertyToBooleanAndWriteBack(CodegenConstants.USE_ONEOF_DISCRIMINATOR_LOOKUP));
        }
        else {
            additionalProperties.put(CodegenConstants.USE_ONEOF_DISCRIMINATOR_LOOKUP, useOneOfDiscriminatorLookup);
        }

        if (additionalProperties.containsKey(CodegenConstants.USE_SINGLE_REQUEST_PARAMETER)) {
            this.setUseSingleRequestParameter(convertPropertyToBoolean(CodegenConstants.USE_SINGLE_REQUEST_PARAMETER));
        }
        writePropertyBack(CodegenConstants.USE_SINGLE_REQUEST_PARAMETER, getUseSingleRequestParameter());

        if (additionalProperties.containsKey(CONFIG_KEY)) {
            this.setConfigKey(additionalProperties.get(CONFIG_KEY).toString());
        }

        if (additionalProperties.containsKey(ASYNC_NATIVE)) {
            this.setAsyncNative(convertPropertyToBooleanAndWriteBack(ASYNC_NATIVE));
        }

        if (additionalProperties.containsKey(PARCELABLE_MODEL)) {
            this.setParcelableModel(Boolean.parseBoolean(additionalProperties.get(PARCELABLE_MODEL).toString()));
        }
        // put the boolean value back to PARCELABLE_MODEL in additionalProperties
        additionalProperties.put(PARCELABLE_MODEL, parcelableModel);

        if (additionalProperties.containsKey(USE_BEANVALIDATION)) {
            this.setUseBeanValidation(convertPropertyToBooleanAndWriteBack(USE_BEANVALIDATION));
        }

        if (additionalProperties.containsKey(PERFORM_BEANVALIDATION)) {
            this.setPerformBeanValidation(convertPropertyToBooleanAndWriteBack(PERFORM_BEANVALIDATION));
        }

        if (additionalProperties.containsKey(USE_GZIP_FEATURE)) {
            this.setUseGzipFeature(convertPropertyToBooleanAndWriteBack(USE_GZIP_FEATURE));
        }

        if (additionalProperties.containsKey(USE_RUNTIME_EXCEPTION)) {
            this.setUseRuntimeException(convertPropertyToBooleanAndWriteBack(USE_RUNTIME_EXCEPTION));
        }

        if (additionalProperties.containsKey(USE_REFLECTION_EQUALS_HASHCODE)) {
            this.setUseReflectionEqualsHashCode(convertPropertyToBooleanAndWriteBack(USE_REFLECTION_EQUALS_HASHCODE));
        }

        if (additionalProperties.containsKey(USE_ABSTRACTION_FOR_FILES)) {
            this.setUseAbstractionForFiles(convertPropertyToBooleanAndWriteBack(USE_ABSTRACTION_FOR_FILES));
        }

        if (additionalProperties.containsKey(DYNAMIC_OPERATIONS)) {
            this.setDynamicOperations(Boolean.parseBoolean(additionalProperties.get(DYNAMIC_OPERATIONS).toString()));
        }
        additionalProperties.put(DYNAMIC_OPERATIONS, dynamicOperations);

        if (additionalProperties.containsKey(SUPPORT_STREAMING)) {
            this.setSupportStreaming(Boolean.parseBoolean(additionalProperties.get(SUPPORT_STREAMING).toString()));
        }
        additionalProperties.put(SUPPORT_STREAMING, supportStreaming);

        if (additionalProperties.containsKey(GRADLE_PROPERTIES)) {
            this.setGradleProperties(additionalProperties.get(GRADLE_PROPERTIES).toString());
        }
        additionalProperties.put(GRADLE_PROPERTIES, gradleProperties);

        if (additionalProperties.containsKey(ERROR_OBJECT_TYPE)) {
            this.setErrorObjectType(additionalProperties.get(ERROR_OBJECT_TYPE).toString());
        }
        additionalProperties.put(ERROR_OBJECT_TYPE, errorObjectType);

        final String invokerFolder = (sourceFolder + '/' + invokerPackage).replace(".", "/");
        final String modelsFolder = (sourceFolder + File.separator + modelPackage().replace('.', File.separatorChar)).replace('/', File.separatorChar);
        authFolder = (sourceFolder + '/' + invokerPackage + ".auth").replace(".", "/");

        //Common files
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md").doNotOverwrite());
        supportingFiles.add(new SupportingFile("build.gradle.mustache", "", "build.gradle").doNotOverwrite());
        supportingFiles.add(new SupportingFile("settings.gradle.mustache", "", "settings.gradle").doNotOverwrite());
        supportingFiles.add(new SupportingFile("gradle.properties.mustache", "", "gradle.properties").doNotOverwrite());
        supportingFiles.add(new SupportingFile("ApiClient.mustache", invokerFolder, "ApiClient.java"));

        if (dynamicOperations) {
            supportingFiles.add(new SupportingFile("openapi.mustache", projectFolder + "/resources/openapi", "openapi.yaml"));
            supportingFiles.add(new SupportingFile("apiOperation.mustache", invokerFolder, "ApiOperation.java"));
        }
        else {
            supportingFiles.add(new SupportingFile("openapi.mustache", "api", "openapi.yaml"));
        }

        supportingFiles.add(new SupportingFile("StringUtil.mustache", invokerFolder, "StringUtil.java"));

        // google-api-client doesn't use the OpenAPI auth, because it uses Google Credential directly (HttpRequestInitializer)
        supportingFiles.add(new SupportingFile("auth/HttpBasicAuth.mustache", authFolder, "HttpBasicAuth.java"));
        supportingFiles.add(new SupportingFile("auth/HttpBearerAuth.mustache", authFolder, "HttpBearerAuth.java"));
        supportingFiles.add(new SupportingFile("auth/ApiKeyAuth.mustache", authFolder, "ApiKeyAuth.java"));

        supportingFiles.add(new SupportingFile("gradlew.mustache", "", "gradlew"));
        supportingFiles.add(new SupportingFile("gradle-wrapper.properties.mustache",
                gradleWrapperPackage.replace(".", File.separator), "gradle-wrapper.properties"));
        supportingFiles.add(new SupportingFile("gradle-wrapper.jar",
                gradleWrapperPackage.replace(".", File.separator), "gradle-wrapper.jar"));
        supportingFiles.add(new SupportingFile("gitignore.mustache", "", ".gitignore"));

        if (performBeanValidation) {
            supportingFiles.add(new SupportingFile("BeanValidationException.mustache", invokerFolder,
                    "BeanValidationException.java"));
        }

        //TODO: add auto-generated doc to feign
        modelDocTemplateFiles.remove("model_doc.mustache");
        apiDocTemplateFiles.remove("api_doc.mustache");
        //Templates to decode response headers
        supportingFiles.add(new SupportingFile("model/ApiResponse.mustache", modelsFolder, "ApiResponse.java"));
        supportingFiles.add(new SupportingFile("ApiResponseDecoder.mustache", invokerFolder, "ApiResponseDecoder.java"));

        // TODO remove "file" from reserved word list as feign client doesn't support using `baseName`
        // as the parameter name yet
        reservedWords.remove("file");

        if (FEIGN.equals(getLibrary())) {
            supportingFiles.add(new SupportingFile("ParamExpander.mustache", invokerFolder, "ParamExpander.java"));
            supportingFiles.add(new SupportingFile("EncodingUtils.mustache", invokerFolder, "EncodingUtils.java"));
            supportingFiles.add(new SupportingFile("auth/DefaultApi20Impl.mustache", authFolder, "DefaultApi20Impl.java"));
        }
        else {
            LOGGER.error("Unknown library option (-l/--library): {}", getLibrary());
        }

        additionalProperties.put(SERIALIZATION_LIBRARY_JACKSON, "true");
        additionalProperties.remove(SERIALIZATION_LIBRARY_GSON);
        additionalProperties.remove(SERIALIZATION_LIBRARY_JSONB);
        supportingFiles.add(new SupportingFile("RFC3339DateFormat.mustache", invokerFolder, "RFC3339DateFormat.java"));

        // authentication related files
        // has OAuth defined
        if (ProcessUtils.hasOAuthMethods(openAPI)) {
            // Add OauthPasswordGrant.java and OauthClientCredentialsGrant.java for feign library
            supportingFiles.add(new SupportingFile("auth/OauthPasswordGrant.mustache", authFolder, "OauthPasswordGrant.java"));
            supportingFiles.add(new SupportingFile("auth/OauthClientCredentialsGrant.mustache", authFolder, "OauthClientCredentialsGrant.java"));
            supportingFiles.add(new SupportingFile("auth/ApiErrorDecoder.mustache", authFolder, "ApiErrorDecoder.java"));
        }
    }


    @Override
    public void postProcess() {
        System.out.println("API generated");
    }

    @Override
    public OperationsMap postProcessOperationsWithModels(OperationsMap objs, List<ModelMap> allModels) {
        super.postProcessOperationsWithModels(objs, allModels);

        // camelize path variables for Feign client
        OperationMap operations = objs.getOperations();
        List<CodegenOperation> operationList = operations.getOperation();
        Pattern methodPattern = Pattern.compile("^(.*):([^:]*)$");
        for (CodegenOperation op : operationList) {

            objs.put("foobar_entity_type", op.baseName.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase());

            String path = op.path;
            String method = "";

            // if a custom method is found at the end of the path, cut it off for later
            Matcher m = methodPattern.matcher(path);
            if (m.find()) {
                path = m.group(1);
                method = m.group(2);
            }

            String[] items = path.split("/", -1);

            for (int i = 0; i < items.length; ++i) {
                if (items[i].matches("^\\{(.*)}$")) { // wrap in {}
                    // camelize path variable
                    items[i] = "{" + camelize(items[i].substring(1, items[i].length() - 1), true) + "}";
                }
            }
            op.path = StringUtils.join(items, "/");
            // Replace the custom method on the path if one was found earlier
            if (!method.isEmpty()) {
                op.path += ":" + method;
            }
        }

        return objs;
    }


    @Override
    public void postProcessModelProperty(CodegenModel model, CodegenProperty property) {
        super.postProcessModelProperty(model, property);
        if (!BooleanUtils.toBoolean(model.isEnum)) {
            //final String lib = getLibrary();
            //Needed imports for Jackson based libraries
            model.imports.add("JsonProperty");
            model.imports.add("JsonValue");
            model.imports.add("JsonInclude");
            model.imports.add("JsonTypeName");
        }
        else { // enum class
            //Needed imports for Jackson's JsonCreator
            model.imports.add("JsonValue");
            model.imports.add("JsonCreator");
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public ModelsMap postProcessModels(ModelsMap objs) {
        objs = super.postProcessModels(objs);
        List<ModelMap> models = objs.getModels();

        List<Map<String, String>> imports = objs.getImports();
        for (ModelMap mo : models) {
            CodegenModel cm = mo.getModel();
            boolean addImports = false;

            for (CodegenProperty var : cm.vars) {
                if (this.openApiNullable) {
                    boolean isOptionalNullable = Boolean.FALSE.equals(var.required) && Boolean.TRUE.equals(var.isNullable);
                    // only add JsonNullable and related imports to optional and nullable values
                    addImports |= isOptionalNullable;
                    var.getVendorExtensions().put("x-is-jackson-optional-nullable", isOptionalNullable);
                }

                if (Boolean.TRUE.equals(var.getVendorExtensions().get("x-enum-as-string"))) {
                    // treat enum string as just string
                    var.datatypeWithEnum = var.dataType;

                    if (StringUtils.isNotEmpty(var.defaultValue)) { // has default value
                        String defaultValue = var.defaultValue.substring(var.defaultValue.lastIndexOf('.') + 1);
                        for (Map<String, Object> enumVars : (List<Map<String, Object>>) var.getAllowableValues().get("enumVars")) {
                            if (defaultValue.equals(enumVars.get("name"))) {
                                // update default to use the string directly instead of enum string
                                var.defaultValue = (String) enumVars.get("value");
                            }
                        }
                    }

                    // add import for Set, HashSet
                    cm.imports.add("Set");
                    Map<String, String> importsSet = new HashMap<>();
                    importsSet.put("import", "java.util.Set");
                    imports.add(importsSet);
                    Map<String, String> importsHashSet = new HashMap<>();
                    importsHashSet.put("import", "java.util.HashSet");
                    imports.add(importsHashSet);
                }

            }

            if (addImports) {
                Map<String, String> imports2Classnames = new HashMap<>();
                imports2Classnames.put("JsonNullable", "org.openapitools.jackson.nullable.JsonNullable");
                imports2Classnames.put("NoSuchElementException", "java.util.NoSuchElementException");
                imports2Classnames.put("JsonIgnore", "com.fasterxml.jackson.annotation.JsonIgnore");
                for (Map.Entry<String, String> entry : imports2Classnames.entrySet()) {
                    cm.imports.add(entry.getKey());
                    Map<String, String> importsItem = new HashMap<>();
                    importsItem.put("import", entry.getValue());
                    imports.add(importsItem);
                }
            }
        }

        // add implements for serializable/parcelable to all models
        for (ModelMap mo : models) {
            CodegenModel cm = mo.getModel();

            cm.getVendorExtensions().putIfAbsent("x-implements", new ArrayList<String>());
            if (this.parcelableModel) {
                ((ArrayList<String>) cm.getVendorExtensions().get("x-implements")).add("Parcelable");
            }
        }

        return objs;
    }

    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        generateYAMLSpecFile(objs);
        return super.postProcessSupportingFileData(objs);
    }

    @Override
    public String toApiVarName(String name) {
        String apiVarName = super.toApiVarName(name);
        if (reservedWords.contains(apiVarName)) {
            apiVarName = escapeReservedWord(apiVarName);
        }
        return apiVarName;
    }

    @Override
    public void addImportsToOneOfInterface(List<Map<String, String>> imports) {
        for (String i : Arrays.asList("JsonSubTypes", "JsonTypeInfo", "JsonIgnoreProperties")) {
            Map<String, String> oneImport = new HashMap<>();
            oneImport.put("import", importMapping.get(i));
            if (!imports.contains(oneImport)) {
                imports.add(oneImport);
            }
        }
    }

    @Override
    public List<VendorExtension> getSupportedVendorExtensions() {
        List<VendorExtension> extensions = super.getSupportedVendorExtensions();
        extensions.add(VendorExtension.X_WEBCLIENT_BLOCKING);
        return extensions;
    }

    // =============================================================

    public void setUseOneOfDiscriminatorLookup(boolean useOneOfDiscriminatorLookup) {
        this.useOneOfDiscriminatorLookup = useOneOfDiscriminatorLookup;
    }

    private boolean getUseSingleRequestParameter() {
        return useSingleRequestParameter;
    }

    private void setUseSingleRequestParameter(boolean useSingleRequestParameter) {
        this.useSingleRequestParameter = useSingleRequestParameter;
    }

    public void setAsyncNative(boolean asyncNative) {
        this.asyncNative = asyncNative;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public void setParcelableModel(boolean parcelableModel) {
        this.parcelableModel = parcelableModel;
    }

    public void setUseBeanValidation(boolean useBeanValidation) {
        this.useBeanValidation = useBeanValidation;
    }

    public void setPerformBeanValidation(boolean performBeanValidation) {
        this.performBeanValidation = performBeanValidation;
    }

    public void setUseGzipFeature(boolean useGzipFeature) {
        this.useGzipFeature = useGzipFeature;
    }

    public void setUseRuntimeException(boolean useRuntimeException) {
        this.useRuntimeException = useRuntimeException;
    }

    public void setUseReflectionEqualsHashCode(boolean useReflectionEqualsHashCode) {
        this.useReflectionEqualsHashCode = useReflectionEqualsHashCode;
    }

    public void setUseAbstractionForFiles(boolean useAbstractionForFiles) {
        this.useAbstractionForFiles = useAbstractionForFiles;
    }

    public void setDynamicOperations(final boolean dynamicOperations) {
        this.dynamicOperations = dynamicOperations;
    }

    public void setSupportStreaming(final boolean supportStreaming) {
        this.supportStreaming = supportStreaming;
    }

    public void setGradleProperties(final String gradleProperties) {
        this.gradleProperties = gradleProperties;
    }

    public void setErrorObjectType(final String errorObjectType) {
        this.errorObjectType = errorObjectType;
    }

}
