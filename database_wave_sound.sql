CREATE TABLE `usuarios` (
`id_usuario` bigint NOT NULL AUTO_INCREMENT,
`contrasena` tinyblob NOT NULL,
`correo` varchar(255) NOT NULL,
`fecha_registro` datetime(6) NOT NULL,
`nombre_usuario` varchar(255) NOT NULL,
`rol` varchar(255) NOT NULL,
                            PRIMARY KEY (`id_usuario`),
UNIQUE KEY `UK2mlfr087gb1ce55f2j87o74t` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

CREATE TABLE Categorias (
    id_categoria INT PRIMARY KEY AUTO_INCREMENT,
    nombre_categoria VARCHAR(255) NOT NULL
);

CREATE TABLE Productos (
id_producto INT PRIMARY KEY AUTO_INCREMENT,
nombre_producto VARCHAR(255) NOT NULL,
precio DECIMAL NOT NULL,
descripcion VARCHAR(5000),
imagen VARCHAR(255),
id_categoria INT,
FOREIGN KEY (id_categoria) REFERENCES Categorias(id_categoria)
);

CREATE TABLE Carritos (
    id_carrito INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario bigint,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Carrito_Productos (
id_carrito_producto INT PRIMARY KEY AUTO_INCREMENT,
id_carrito INT NOT NULL,
id_producto INT NOT NULL,
cantidad INT NOT NULL,
precio_unitario DECIMAL(10,2) NOT NULL,
FOREIGN KEY (id_carrito) REFERENCES Carritos(id_carrito),
FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);


CREATE TABLE Inventarios (
id_inventario INT PRIMARY KEY AUTO_INCREMENT,
 id_producto INT UNIQUE,
cantidad_disponible INT NOT NULL,
ultima_actualizacion DATETIME DEFAULT CURRENT_TIMESTAMP,
estado BOOLEAN,
FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

CREATE TABLE Pedidos (
    id_pedido INT PRIMARY KEY AUTO_INCREMENT,
    id_usuario BIGINT,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado BOOLEAN,
    direccion_envio VARCHAR(5000)
    total DOUBLE,
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

CREATE TABLE Pedido_Detalle (
    id_pedido_detalle INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    id_producto INT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2),
    subtotal DECIMAL(10,2),
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES Productos(id_producto)
);

CREATE TABLE Facturas (
    id_factura INT PRIMARY KEY AUTO_INCREMENT,
    id_pedido INT,
    fecha_emision DATETIME DEFAULT CURRENT_TIMESTAMP,
    monto_total DOUBLE,
    impuesto DOUBLE,
    FOREIGN KEY (id_pedido) REFERENCES Pedidos(id_pedido)
);

--Insert en categorias

INSERT INTO categorias (id_categoria, nombre_categoria) VALUES
(1, 'Guitarras'),
(2, 'Teclados y Pianos'),
(3, 'Percusión'),
(4, 'Cuerdas'),
(5, 'Viento'),
(6, 'Accesorios'),
(7, 'Equipos de Sonido'),
(8, 'Grabación y Estudio'),
(9, 'Micrófonos'),
(10, 'DJ y Producción Musical');