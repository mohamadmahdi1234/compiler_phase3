.data 
	true: .asciiz "true"
	false : .asciiz "false"

	error_run_time: .asciiz "runtime ERROR"
	StringLiteral_01: .asciiz "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent convallis sed metus eget fermentum. Mauris in finibus ligula. Quisque sit amet porttitor nisi, sed molestie purus. Praesent facilisis augue in dolor rhoncus, eu ornare nisl faucibus. Vivamus tincidunt erat nec nunc dictum dapibus. Sed eget sollicitudin dolor. Aliquam dui dolor, euismod at eleifend et, viverra sed tortor. Mauris mi odio, efficitur a nisl nec, aliquet luctus ex. In ut tortor non orci tincidunt viverra sed mollis nibh. Proin nec mi velit. Nulla vitae lorem magna. Sed eu odio efficitur, mattis diam ornare, convallis magna. Praesent malesuada nulla eu arcu varius, quis pellentesque lacus feugiat. Aliquam tempor sem vitae enim finibus mattis. Quisque nec porttitor ipsum, nec pellentesque arcu. Mauris id ante in nisl lobortis rhoncus eu id erat. Cras interdum quis felis et congue. Morbi hendrerit nunc quam, vel semper lorem porta quis. Morbi tristique enim ac dapibus pharetra. Pellentesque sit amet tincidunt ipsum. Morbi molestie, magna quis volutpat sollicitudin, enim metus tincidunt massa, a ultricies lectus est tristique massa. Nulla sed tempor metus, nec pretium metus. Quisque finibus iaculis porta. Mauris vel finibus metus, eget pretium tortor. Mauris gravida tellus ut pellentesque scelerisque. Nunc interdum metus mi, tincidunt posuere augue congue non. Aliquam erat volutpat. Nam ut placerat nibh. Morbi pretium at turpis in tempus. Pellentesque malesuada purus nec lorem consequat suscipit. Proin in sem et tortor commodo pulvinar non et velit. Donec sodales neque non euismod rutrum. Morbi vitae arcu sed diam consequat malesuada vel et lacus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Phasellus eget interdum dui, vel aliquet neque. Nullam lobortis et nulla sit amet venenatis. Praesent fermentum, diam id rhoncus luctus, tortor elit euismod elit, nec posuere sem lorem eu ipsum. Quisque elementum fermentum justo, et scelerisque odio auctor quis. Phasellus ut eros sed nunc ullamcorper cursus. Duis eu aliquet nisi. Curabitur et venenatis velit. Donec cursus consectetur libero, in ultrices mauris pretium a. Vestibulum sed tempus quam. Mauris mattis egestas odio nec aliquet. Pellentesque pulvinar egestas augue nec sodales. Nunc at venenatis neque, et luctus tellus. Curabitur vel malesuada sapien. Morbi sit amet tincidunt justo, quis porttitor diam. Quisque fermentum cursus felis, ut dictum ex egestas eget. In maximus, nulla sed rhoncus maximus, lacus mauris efficitur quam, laoreet auctor diam massa a justo. Sed scelerisque congue volutpat. Nulla nisi sapien, euismod venenatis urna et, elementum interdum velit. Suspendisse congue orci vitae bibendum luctus. Maecenas arcu sapien, vehicula at gravida feugiat, lacinia nec risus. Morbi non ipsum eget turpis ullamcorper dictum at bibendum est. Vestibulum mattis lacus in fringilla gravida. Phasellus facilisis metus sit amet ligula facilisis, vitae eleifend metus faucibus. Pellentesque ullamcorper nec dolor vel pharetra. Vivamus dictum leo pulvinar ex volutpat venenatis non at leo. Aliquam ac viverra lectus. Pellentesque nibh libero, interdum eget arcu sed, mattis posuere arcu. Nam dignissim a risus sed auctor. Nunc rhoncus urna ac gravida vestibulum. Fusce aliquet, ex sed feugiat pulvinar, eros arcu porta arcu, non rutrum nisl turpis at ante. Sed sit amet ligula porta, pellentesque nibh vitae, fringilla augue. Interdum et malesuada fames ac ante ipsum primis in faucibus. Morbi dignissim enim eu nunc vehicula elementum. Donec ut tempor metus, mattis egestas justo. Fusce nec mi nisl. Nulla sed metus magna. Duis tempus mattis mi et fermentum. Praesent accumsan diam eu erat commodo, fermentum dictum nisl aliquam. Proin luctus arcu ac erat faucibus pretium. Praesent faucibus pellentesque blandit. Quisque non blandit nisi. Aenean vestibulum sapien in arcu bibendum, a dapibus nibh malesuada. Mauris interdum sem aliquam sagittis vestibulum. Vestibulum sollicitudin metus eu laoreet semper. Donec ornare sapien id luctus posuere. Integer rutrum, leo id sollicitudin pharetra."

.text
	.globl main

	main:
		jal global_main
		#END OF PROGRAM
		li $v0,10
		syscall
	runtime_error:
		li $v0, 4
		la $a0, error_run_time
		syscall
		#END OF PROGRAM
		li $v0,10
		syscall
	global_main:
		sw $ra,0($sp)
		addi $sp,$sp,4
		la $t0, StringLiteral_01
		li $v0, 4
		add $a0, $t0, $zero
		syscall
		#print new Line
		addi $a0, $0, 0xA
		addi $v0, $0, 0xB
		syscall 
		addi $sp,$sp,-4
		lw $ra,0($sp)
		jr $ra
