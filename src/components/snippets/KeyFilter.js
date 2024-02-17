import {
    Button,
    Card,
    CardBody,
    CardHeader,
    CardTitle, Col, Collapse,
    Container, DropdownDivider,
    Form,
    FormCheck,
    FormControl,
    FormLabel, Row, ToggleButton
} from "react-bootstrap";
import FormCheckLabel from "react-bootstrap/FormCheckLabel";
import {useState} from "react";

export const KeyFilter = () => {

    const [isOpen, setOpen] = useState(true)

    return (
        <Container
            className='mt-5'>
            <Button
                className='w-100 d-md-none mb-2'
                aria-expanded={isOpen}
                onClick={() => {
                    setOpen(!isOpen)
                }}>
                Фильтры
            </Button>
            <Collapse
                in={isOpen}
            >
                <div>
                    <Card>
                        <CardHeader>
                            <CardTitle>Фильтр ключей</CardTitle>
                        </CardHeader>
                        <CardBody>
                            <Form
                                className='container-fluid'
                                onSubmit={(e) => {
                                    e.preventDefault();
                                    console.log('Фильтрация ключей')
                                }}>
                                <Row>
                                    <Col
                                        lg={3}
                                        sm={6}>
                                        <FormControl
                                            placeholder={'Номер здания'}
                                            type='number'/>
                                    </Col>
                                    <Col
                                        lg={3}
                                        sm={6}>
                                        <FormControl
                                            className='mt-2 mt-sm-0'
                                            placeholder={'Номер аудитории'}
                                            type='number'/>
                                    </Col>
                                    <Col
                                        lg={2}
                                        sm={4}
                                        xs={6}
                                    >
                                        <FormCheck
                                            className='mt-2 mt-lg-0'
                                            type={'switch'}
                                            id={'inDaenery'}
                                            label={'В деканате'}/>
                                    </Col>
                                    <Col
                                        lg={2}
                                        sm={4}
                                        xs={6}
                                    >
                                        <FormCheck
                                            className='mt-2 mt-lg-0'
                                            type={'switch'}
                                            id={'inHands'}
                                            label={'На руках'}/>
                                    </Col>
                                    <Col
                                        lg={2}
                                        sm={4}>
                                        <Button
                                            className='mt-2 mt-lg-0 w-100'
                                            type='submit'>
                                            Найти
                                        </Button>
                                    </Col>
                                </Row>
                            </Form>
                        </CardBody>
                    </Card>
                </div>
            </Collapse>

        </Container>
    )
}